package edu.emory.mathcs.nlp.deeplearning;

import java.io.Serializable;


/**
 * Hello world!
 *
 */
@SuppressWarnings("serial")
public class SVMClassifier implements Serializable
{
	public void test()
	{
//		System.out.println("AA");
//		SparkConf conf = new SparkConf().setAppName("SVM Classifier");
//		System.out.println("BB");
//		SparkContext sc = new SparkContext(conf);
//		System.out.println("CC");
//		String path = "dat/sample_libsvm_data.txt";
//		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();
//		System.out.println("DD");
//		
//		JavaRDD<LabeledPoint> training = data.sample(false, 0.6, 11);
//		training.cache();
//		JavaRDD<LabeledPoint> test = data.subtract(training);
//		
//		int numIterations = 1;
//		final SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);
//		model.clearThreshold();
//		
//		JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test.map(
//			new Function<LabeledPoint,Tuple2<Object,Object>>() {
//				public Tuple2<Object,Object> call(LabeledPoint p) {
//					double score = model.predict(p.features());
//					size++;
//					if (score >= 0){if (p.label() > 0) correct++;}
//					else if (p.label() == 0) correct++;
//					System.out.println(size+" "+correct);
//					return new Tuple2<Object, Object>(score, p.label());
//				}
//			}
//		);
//		
//		BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));
//		double auROC = metrics.areaUnderROC();
//		System.out.println("Area under ROC = " + auROC);
		
//		model.save(sc, "myModelPath");
//		SVMModel sameModel = SVMModel.load(sc, "myModelPath");
		
				
				
		
		
//		BasicNetwork network = new BasicNetwork();
//		// input layer
//		network.addLayer(new BasicLayer(null,true,2));
//		// hidden layer
//		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
//		// output layer
//		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,2));
//		// no more layers will be added
//		network.getStructure().finalizeStructure();
//		// randomize the weights in the connections between the layers
//		network.reset(5);
//		
//		// XOR example
//		double input[][] = {{0,0},{1,0},{0,1},{1,1}};
//		double gold[][] =  {{1,0},{0,1},{0,1},{1,0}};
//		
//		MLDataSet trainingSet = new BasicMLDataSet(input, gold);
//		MLTrain train = new ResilientPropagation (network, trainingSet);
//		int epoch = 1;
//		
//		do
//		{
//			train.iteration();
//			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
//			epoch++;
//		}
//		while (train.getError() > 0.01);
//
//		System.out.println("Neural Network Results:");
//
//		for (MLDataPair pair : trainingSet )
//		{
//			final MLData output = network.compute(pair.getInput());
//			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)  + " | gold=" + pair.getIdeal().getData(0) + "," + pair.getIdeal().getData(1) + ", actual=" + output. getData(0) + "," + output.getData(1));
//		}
	}
	
    public static void main( String[] args )
    {
        new SVMClassifier().test();
    }
}
