package com.illidan.dengqian.bg220.tool_bean.voice;

import android.media.AudioFormat;
import android.media.AudioRecord;

import com.illidan.dengqian.bg220.MainActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AudioRecordManager {
    public static final String TAG = "AudioRecordManager";
    private AudioRecord mRecorder;
    private DataOutputStream dos;
    private Thread recordThread;
    public static boolean isStart = false;
    private static AudioRecordManager mInstance;
    private  int bufferSize;
    private static String pcm_path;

    private int sampleRatelnHz=44100;
    private int mChannel=AudioFormat.CHANNEL_IN_STEREO;
    private int mEncoding=AudioFormat.ENCODING_PCM_16BIT;
    public static int record_int=1;
    public static final String []record_type_remark={
            "DEFAULT 默认","MIC 麦克","VOICE_UPLINK 上行","VOICE_DOWNLINK 下行","VOICE_CALL 上下行","CAMCORDER 相机","VOICE_RECOGNITION 语音识别调整音","VOICE_COMMUNICATION对Voip 调整","REMOTE_SUBMIX 远端混合","UNPROCESSED 原始麦克"
    };


    public AudioRecordManager() {
            bufferSize = AudioRecord.getMinBufferSize(sampleRatelnHz, mChannel, mEncoding);
            bufferSize=bufferSize*4;
            mRecorder = new AudioRecord(record_int, sampleRatelnHz, mChannel, mEncoding, bufferSize);
    }


    /**
     * 获取单例引用
     *
     * @return
     */
    public static AudioRecordManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecordManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecordManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 销毁线程方法
     */
    private void destroyThread() {
        try {
            isStart = false;
            if (null != recordThread && Thread.State.RUNNABLE == recordThread.getState()) {
                try {
                    recordThread.interrupt();
                } catch (Exception e) {
                    recordThread = null;
                }
            }
            recordThread = null;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            recordThread = null;
        }
    }

    /**
     * 启动录音线程
     */
    private void startThread() {

        destroyThread();
        isStart = true;
        if (recordThread == null) {
            recordThread = new Thread(recordRunnable);
            recordThread.start();

        }
    }

    /**
     * 录音线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                int bytesRecord;

                byte[] audioDataA = new byte[bufferSize];


                if (mRecorder.getState() != AudioRecord.STATE_INITIALIZED) {

                        stopRecord();


                    return;
                }
                mRecorder.startRecording();
                //writeToFileHead();

                while (isStart) {
                    if (null != mRecorder) {
                        bytesRecord = mRecorder.read(audioDataA, 0, bufferSize);
                        if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (bytesRecord != 0 && bytesRecord != -1) {
                            //在此可以对录制音频的数据进行二次处理 比如变声，压缩，降噪，增益等操作
                            //我们这里直接将pcm音频原数据写入文件 这里可以直接发送至服务器 对方采用AudioTrack进行播放原数据
                            dos.write(audioDataA);
                        } else {
                            break;
                        }
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    };

    /**
     * 保存文件
     *
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        dos = new DataOutputStream(new FileOutputStream(file, true));
    }

    /**
     * 录音
     * @param dict_path
     * @param file_name
     */
    public void startRecord(String dict_path,String file_name) {

        file_name=record_type_remark[record_int]+"_"+file_name;
        String path=dict_path+file_name;
        FileUnit.makeRootDirectory(dict_path);
        try {
            pcm_path=path;
            setPath(path);
            startThread();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        try {
            destroyThread();
            if (mRecorder != null) {
                if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    mRecorder.stop();
                }
                if (mRecorder != null) {
                    mRecorder.release();
                }
            }
            if (dos != null) {
                dos.flush();
                dos.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }finally {
            Thread th= new Thread(new Runnable() {
                @Override
                public void run() {
                    PcmToWavUtil p2w=new PcmToWavUtil(sampleRatelnHz,mChannel,mEncoding);
                    p2w.pcmToWav(pcm_path,pcm_path+".wav");
                }
            });
            th.start();


        }
    }




}




