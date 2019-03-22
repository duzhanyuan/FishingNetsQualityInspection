package org.jaiken.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

/**
 * @TODO 加载训练结果模型，并进行单张图片的分类和检测
 * @author Administrator
 *
 */
public class FishNetSingleClassification {

    protected  static int height=180;
    protected  static int width=180;
    protected  static int channels=3;
    protected static long seed = 50;
    protected static int batchSize = 10;
    protected static Random rng = new Random(seed);
    protected static int epochs = 150;
    protected static boolean save = true;
    protected  static int outputNum=2;
    //protected static final Logger log= LoggerFactory.getLogger(FishNetTrainForModel.class);
    protected static  MultiLayerNetwork network;


    public  void detectWrong(File file) throws IOException {
        if (network == null) {
            loadModel();
        }
        File trainData = file;
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS,true);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        ImageRecordReader recordReader = new ImageRecordReader(height,width,channels,labelMaker);
        recordReader.initialize(train);
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader,batchSize,1,outputNum);
        DataNormalization scaler = new ImagePreProcessingScaler(0,1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        DataSet img = dataIter.next();
        List<String> allClassLabels = recordReader.getLabels();
        //int labelIndex = img.getLabels().argMax(1).getInt(0);
        int[] predictedClasses = network.predict(img.getFeatures());
        String modelPrediction = allClassLabels.get(predictedClasses[0]);
        System.out.println("modelPrediction:"+modelPrediction);

    }
    public void loadModel() throws IOException {

        String modelPath = FilenameUtils.concat(System.getProperty("user.dir"), "src/main/resources/model.bin");
        network =ModelSerializer.restoreMultiLayerNetwork(new File(modelPath),false);
    }
    public FishNetSingleClassification(){
        //初始化加载模型
        try{
            loadModel();
        }catch (Exception e){

        }
    }
    public static void main(String[] args){
        FishNetSingleClassification fishNetSingleClassification=new FishNetSingleClassification();
        try {
            fishNetSingleClassification.detectWrong(new File(System.getProperty("user.dir")+"/src/main/resources/FishNetTest"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
