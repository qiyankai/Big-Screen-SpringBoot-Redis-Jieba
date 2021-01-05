package com.qianxinyao.analysis.jieba.keyword;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huaban.analysis.jieba.JiebaSegmenter;

/**
 * @author Tom Qian
 * @email tomqianmaple@outlook.com
 * @github https://github.com/bluemapleman
 * @date Oct 20, 2018
 * tfidf算法原理参考：http://www.cnblogs.com/ywl925/p/3275878.html
 * 部分实现思路参考jieba分词：https://github.com/fxsjy/jieba
 */
public class TFIDFAnalyzer
{
	
	static HashMap<String,Double> idfMap;
	static HashSet<String> stopWordsSet;
	static double idfMedian;
	
	/**
	 * tfidf分析方法
	 * @param content 需要分析的文本/文档内容
	 * @param topN 需要返回的tfidf值最高的N个关键词，若超过content本身含有的词语上限数目，则默认返回全部
	 * @return
	 */
	public List<Keyword> analyze(String content,int topN){
		List<Keyword> keywordList=new ArrayList<>();
		
//		if(stopWordsSet==null) {
			stopWordsSet=new HashSet<>(2000);
			loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/stop_words.txt"));
//		}
		if(idfMap==null) {
			idfMap=new HashMap<>(300000);
			loadIdfMap(idfMap, this.getClass().getResourceAsStream("/idf_dict.txt"));
		}
		
		Map<String, Double> tfMap=getTf(content);
		for(String word:tfMap.keySet()) {
			// 若该词不在idf文档中，则使用平均的idf值(可能定期需要对新出现的网络词语进行纳入)
			if(idfMap.containsKey(word)) {
				keywordList.add(new Keyword(word,idfMap.get(word)*tfMap.get(word)));
			}else{
				keywordList.add(new Keyword(word,idfMedian*tfMap.get(word)));
			}
		}
		
		Collections.sort(keywordList);
		
		if(keywordList.size()>topN) {
			int num=keywordList.size()-topN;
			for(int i=0;i<num;i++) {
				keywordList.remove(topN);
			}
		}
		return keywordList;
	}
	
	/**
	 * tf值计算公式
	 * tf=N(i,j)/(sum(N(k,j) for all k))
	 * N(i,j)表示词语Ni在该文档d（content）中出现的频率，sum(N(k,j))代表所有词语在文档d中出现的频率之和
	 * @param content
	 * @return
	 */
	private Map<String, Double> getTf(String content) {
		Map<String,Double> tfMap=new HashMap<>(40000);
		if(content==null || "".equals(content)) {
			return tfMap;
		}
		
		JiebaSegmenter segmenter = new JiebaSegmenter();
		List<String> segments=segmenter.sentenceProcess(content);
		Map<String,Integer> freqMap=new HashMap<>(1000);
		
		int wordSum=0;
		for(String segment:segments) {
			//停用词不予考虑，单字词不予考虑
			if(!stopWordsSet.contains(segment.trim()) && segment.length()>1) {
				wordSum++;
				if(freqMap.containsKey(segment)) {
					freqMap.put(segment,freqMap.get(segment)+1);
				}else {
					freqMap.put(segment, 1);
				}
			}
		}
		
		// 计算double型的tf值
		for(String word:freqMap.keySet()) {
			tfMap.put(word,freqMap.get(word)*0.1/wordSum);
		}
		
		return tfMap; 
	}
	
	/**
	 * 默认jieba分词的停词表
	 * url:https://github.com/yanyiwu/nodejieba/blob/master/dict/stop_words.utf8
	 * @param set
	 * @param filePath
	 */
	private void loadStopWords(Set<String> set, InputStream in){
		BufferedReader bufr;
		try
		{
			bufr = new BufferedReader(new InputStreamReader(in));
			String line=null;
			while((line=bufr.readLine())!=null) {
				set.add(line.trim());
			}
			try
			{
				bufr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * idf值本来需要语料库来自己按照公式进行计算，不过jieba分词已经提供了一份很好的idf字典，所以默认直接使用jieba分词的idf字典
	 * url:https://raw.githubusercontent.com/yanyiwu/nodejieba/master/dict/idf.utf8
	 * @param set
	 * @param filePath
	 */
	private void loadIdfMap(Map<String,Double> map, InputStream in ){
		BufferedReader bufr;
		try
		{
			bufr = new BufferedReader(new InputStreamReader(in));
			String line=null;
			while((line=bufr.readLine())!=null) {
				String[] kv=line.trim().split(" ");
				map.put(kv[0],Double.parseDouble(kv[1]));
			}
			try
			{
				bufr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// 计算idf值的中位数
			List<Double> idfList=new ArrayList<>(map.values());
			Collections.sort(idfList);
			idfMedian=idfList.get(idfList.size()/2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		String content="关于吸引民企高管加入党组织 增强高科技人才双向流动的建议\n" +
				
				"案由：根据全国工商联2018年私营企业抽样调查显示，年轻一代私营企业主的入党比例存在下降趋势。60岁以上的私营企业主入党比例为54%，50-59岁为44%，40-49岁为33%，40岁以下仅为31%。就北京市而言，本人创业以来接触的民营企业家、民营企业管理层中入党比例亦与年龄成正比，在更广泛的中小企业中，党员企业家、党员高管比例更低。\n" +
				
				"案据：当前，民营企业实际控制人、高管中党员比例较低北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市北京市，直接影响到企业对党建的重视程度和非公党建工作水平，主要原因有：一、受教育经历和社会阅历部门民营企业实际控制人、高管对党的认识还不充分，尤其是当民营企业主本人不是党员时，主动性降低。二、相对于民营企业的基数，各级党组织对民营企业的关怀还不够，民营企业党建的获得感不够强。三、各级党组织对民营企业党员名额的分配较为苛刻，民营企业骨干符合条件、且有强烈意愿，仍面临着“入党难”、入党手续复杂、时间长的问题。综上原因，从长远来看，可能将出现民营企业党员企业家越来越少、民营企业骨干党员数量越来越少的局面，因此，吸引民营企业骨干加入党组织，增强高科技人才双向流动是我们解决年轻一代民营企业入党难问题的重要抓手。\n" +
				
				"因此，我提出以下建议：\n" +
				
				"一、围绕重点企业吸引民营企业骨干加入党组织。吸引企业当家人、高管加入党组织，关键要让个体有获得感、荣誉感、成就感，在精神层面给予肯定，让企业家切切实实感受到自己是“自家人”。建议在对企业的各级各类优秀评选和表彰中，将企业党建工作纳入相关奖项评价体系；将是否是党员作为评价企业家的辅助北京市条件之一，推动“两代表一委员”和各种先进模范评选名额适当向党员企业家倾斜；给予党员企业家、党员民营企业骨干更多培训发展机会、更多展现个人的舞台。\n" +
				
				"二、强化行业管理，压实发展党员任务责任。民营企业党关系一般在属地，少部分在行业。对更多专注于行业的民营企业来说，行业性社会组织对业务更为熟悉，对企业更有影响力甚至是驱动力。建议强化行业党建责任，让有条件的商协会党组织引导北京市更多民营企业成立党组织。商协会则需将资源向民营企业倾斜，让民北京市营企业有实实在在获得感。商协会要加大在民企中发展党员的力度，在实践中注意发现管理层和骨干中的优秀苗子，开辟专门渠道，不断提高民营企业管理层党员占比。\n" +
				
				"三、健全激励机制，切实调动民企发展党员的积极性。鼓励在单位设立党群活动中心，推动一批示范性党组织，积极打造成为北京市新时代“双强六好”党组织；制定“强党建、促发展”若干举措，同统战部、工商联、市场监管、税务、人才等部门。加强北京市民营企业家的政治引领和政治吸纳，以北京市党组织为渠道落实各项服务企北京市业政策礼包。统筹安排民营企业党组织与国有企业党组织结对共建，开展党建“1+1”活动。增强高科技人才双向流动，既要鼓励民营企业党组织高管到政府部门挂职锻炼。关于加快出台地方金融监管条例 开展“蜜蜂行动”风险教育活动的建议\n" +
				
				"案由：《北京市地方金融监督管理条例》是北京市首部关于地方金融监督管理的地方性法规，不仅填补了北京市地方金融立法的空白，也解决了地方金融主管部门执法缺乏有效手段、执法依据不足的问题。北京加快出台地方金融监管条例，各级金融监管部门、金融机构、宣传部门、新闻媒体、各街道乡镇和基层社区力量都北京市要参与其中。\n" +
				
				"案据：\n" +
				
				"经过调研发现，各省推出的“地方金融监督管理条例”进行对比，发现多数条例将焦点都集中在了金融风险防范和打击非法集资等方面。因为，围绕这两个问题，《北京市地方金融监督管理条例》应重点予以防范。为了解决立法与普法衔接问题，宜将立法工作与普法工作共同推进。当前，“蜜蜂行动”是由北京市地方金融监督管理局组织北京市发起的金融消费者教育保护体系建设系列活动。对防范金融风险、推广地方金融监管条例、提高投资者风险意识有着重要意义。\n" +
				
				"因此，我提出以下建议：\n" +
				
				"第一，提前设置好金融纠纷调解机制，写入《北京市地方金融监督管理条例》，形成配套机制。建议将北京市互联网金融行业协会纳入到金融纠纷多元化解机制建设工作小组，明确其“金融纠纷调解组织”的地位，邀请人大代表担当特邀调解员、人民陪审员，或者人民监督员等职务。在受理和审理金融纠纷案件过程中，应当北京市落实“调解优先、调判结合”方针，在网贷领域纠纷建议先由“北互金”调解中心牵头，对涉及案件进行调解。经金融纠纷调解组织调解员主持调解达成的调解协议，双方签字盖章后人民法院应明确其法律效力。经法院确认有效的调解协议，如另一方拒绝履行的，对方当事人可以申请人民法院强制执行。通过北互金与法院共建的事前调解机制，积极引导先通过调解方式解决纠纷，依法理性维权。\n" +
				
				"第二，加强普法工作，联合市区各级政府、金融监管部门、金融机构、宣传部门、新闻媒体、各街道乡镇和基层社区志愿者力量，开展“蜜蜂行动”风险教育活动。设立全市反诈骗防范金融风险热线，与公安系统反诈骗热线打通，在金融风险发生时提高事前介入的能力，为了北京市更好回应各基层社区居民关于金融风险的热点问题，建议纳入到北京市“市民12345”热线和“北京12345”公众号体系内，后期将宣传资料进驻到各大金融机构以及市区政务服务局，做到网上网下同步开展，构建多维度、多场景、多功能的投资者教育保护体系，将优质公益性投资者教育内容送入各级政府、基层社区和居民百姓，提高基层金融治理体系和治理能力建设，在实践中做到“预防为主，严格打击”。";
		int topN=50;
		TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
		List<Keyword> list=tfidfAnalyzer.analyze(content,topN);
		for(Keyword word:list) {
			System.out.print(word.getName() + ":" + word.getTfidfvalue() + ",");
		}
	}
}

