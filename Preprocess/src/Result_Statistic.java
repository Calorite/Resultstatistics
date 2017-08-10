import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.UIDefaults.LazyValue;

public class Result_Statistic extends JFrame {


	
	public List<OutData> getOutput(String path) {
		List<OutData> textLines = new ArrayList<OutData>();				
		try {
			int id=1;
			InputStreamReader read=new InputStreamReader(new FileInputStream(path),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(read);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (id>1) {
					OutData out=new OutData();
					String[] l=line.split(" ");
					out.id=Integer.valueOf(l[0]);
					out.negative=Double.valueOf(l[1]);
					out.positive=Double.valueOf(l[2]);
					textLines.add(out);
				}
				id++;
			}
			read.close();				
		} catch (Exception e) {
			// TODO: handle exception
		}
		return textLines;
		
  }
	
	public List<Integer> getTestlabel(String path){
		List<Integer> list=new ArrayList<>();
		try {
			
			InputStreamReader read=new InputStreamReader(new FileInputStream(path),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(read);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] l=line.split(" ");
				int id=Integer.valueOf(l[0]);
				list.add(id);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;	
	}
	
	private static final long serialVersionUID = 1L;
    static List<StatisticData> StatisticDatas =new ArrayList<>();

	
	public static void main(String[] args) {
		Result_Statistic rs=new Result_Statistic();
		
		
		for(double threshold=0.5001;threshold<0.5499;threshold=threshold+0.0001){//
			double avpre=0.0;
			double avrecall=0.0;
			for (int i =1; i < 11; i++) {
				if (i!=8) {					
					List<OutData> datalist=rs.getOutput("C:\\Users\\YI\\Desktop\\SVM_Trainingfile\\word2vector\\new\\"+i+"\\out1");
					List<Integer> labellist=rs.getTestlabel("C:\\Users\\YI\\Desktop\\SVM_Trainingfile\\word2vector\\new\\"+i+"\\test.txt");
					Integer[] labels=new Integer[labellist.size()];
					labellist.toArray(labels);
					int sum=0;
					int target=0;
					double precison=0.0;
					
					double recall=0.0;
					
					int trueP=0;
					int falseP=0;
					int trueN = 0;
					int falseN=0;
					int NotCoveredP=0;
					int NotCoveredN=0;
					for (OutData data : datalist) {
						if (labellist.size()>sum) {
							if (data.negative>=threshold) {
								if (data.id==labels[sum]) {
									trueN++;
								}else {
									falseN++;
								}	
								target++;
							}else {
								NotCoveredN++;
							}
							if (data.positive>=threshold) {
								if (data.id==labels[sum]) {
									trueP++;
								}else {
									falseP++;
								}
								target++;
							}else{
								NotCoveredP++;
							}			
						}

						sum++;
					}
					precison=Double.valueOf(trueP)/(Double.valueOf(trueP)+Double.valueOf(falseP));
					recall=Double.valueOf(trueP)/(Double.valueOf(trueP)+Double.valueOf(falseN)+Double.valueOf(NotCoveredP));
					//	System.out.println(i);
					avpre=avpre+precison;
					avrecall=avrecall+recall;
					System.out.println("sum:"+sum+"   threshold:"+threshold);
					//System.out.println("target:"+target+" TP:"+trueP+" FP:"+falseP+" FN:"+falseN+" TN:"+trueN);
					//System.out.println("precision:"+precison);
					//System.out.println("recall:"+recall);
					//System.out.println("F:"+2*recall*precison/(recall+precison));
					System.out.println(i);
					System.out.println("----------------------------------------------");
					if (i==10) {
						StatisticData data=new StatisticData();
						data.precision=avpre/9;
						data.recall=avrecall/9;
						data.F=2*data.recall*data.precision/(data.recall+data.precision);
						data.thershold=threshold;
						StatisticDatas.add(data);
						System.out.println(data.precision);					
					}
				}
			}
		}
		Result_Statistic frame=new Result_Statistic();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(10, 10, 500, 500);
	    frame.setTitle("result");
	    frame.setVisible(true);
		
	}
	 public Result_Statistic() {
		// TODO Auto-generated constructor stub  
		    XYSeriesCollection dataset=new XYSeriesCollection();
		    XYSeries series1=new XYSeries("precision");
		    XYSeries series2=new XYSeries("recall");
		    XYSeries series3=new XYSeries("F");
		    for (StatisticData statisticData : StatisticDatas) {
				series1.add(statisticData.thershold,statisticData.precision*100);
				series2.add(statisticData.thershold,statisticData.recall*100 );
				series3.add(statisticData.thershold, statisticData.F*100);
			}
		    dataset.addSeries(series1);
		    dataset.addSeries(series2);
		    dataset.addSeries(series3);
	        JFreeChart chart=ChartFactory.createXYLineChart("", "probability", "%", dataset);
		   	XYPlot plot=(XYPlot)chart.getPlot();    
		    NumberAxis domainAxis=(NumberAxis)plot.getRangeAxis();
		    ChartPanel cpanel = new ChartPanel(chart);
		    getContentPane().add(cpanel, BorderLayout.CENTER);
	}
}