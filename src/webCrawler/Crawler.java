package webCrawler;

import getContent.GetContent;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.regex.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * @author Zhao Hang
 * @date:2015-3-23����1:07:29
 * @email:1610227688@qq.com
 */
public class Crawler {
	private String title;
	private volatile static int threadNum = 0;
	private int urlCount = 1000;
	private volatile int visitedURL = 0;// ��ʾĿǰ�Ѿ����ʵ���ҳ����
	private int threadCount = 5;// ��ʾ���ͬʱ�������ж��ٸ��߳�
	private double threshold = 0.91;
	private String startURL;
	// ����hashtable��ʹ��֧��ͬ��
	private Hashtable<String, Integer> keyWords = new Hashtable<String, Integer>();
	// �ȴ��������ҳ
	private PriorityBlockingQueue<PriorityURL> waitforHandling = new PriorityBlockingQueue<PriorityURL>();
	// ������ʾ�����Ѿ����ʹ�����ҳ
	private HashSet<String> isWaiting = new HashSet<String>();
	// ��ʾ����Ҫ������Ҫʣ����ʾ����ҳ
	private Hashtable<String, String> wanted = new Hashtable<String, String>();
	// ���Ǳ���Ĳ���ص���ҳ
	private HashSet<String> noneRelevant = new HashSet<String>();

	private boolean stop = false;
	// private boolean finished = false;

	private JTextPane textpane;
	private JLabel label;
	private JButton button;

	ExecutorService threadPool = Executors.newCachedThreadPool();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(int urlCount) {
		this.urlCount = urlCount;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public Enumeration<String> getKeyWords() {
		return keyWords.keys();
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getStartURL() {
		return startURL;
	}

	public void setStartURL(String startURL) {
		this.startURL = startURL;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public void addKeyWord(String word, int count) {
		keyWords.put(word, count);
	}

	public void removeKeyWord(String word) {
		if (word != null) {
			if (keyWords.containsKey(word)) {
				keyWords.remove(word);
			}
		}
	}

	public void removeAllKeyWords() {
		keyWords.clear();
	}

	public Crawler(String title, String start, JTextPane textpane, JLabel label, JButton button) {
		this.title = title;
		this.startURL = start;
		this.textpane = textpane;
		this.label = label;
		this.button = button;
	}

	public void initialize() {
		stop = false;
		// label.setText("����������0");
		Download download = new Download();
		try {
			String content = download.downloadHttp(new URL(startURL));
			String title = "";
			String regex = "<title>([^<]+)</title>";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(content);
			if (m.find())
				title = m.group(1);
			System.out.println("title is " + title);
			int count;
			// ͳ�ƹؼ��ʵĴ�Ƶ
			for (String key : keyWords.keySet()) {
				count = content.split(key).length - 1;
				keyWords.put(key, count);
			}
			for (String key : keyWords.keySet()) {
				System.out.println(key + ":" + keyWords.get(key));
			}
			// ������ض�
			double cos = calRelavancy(keyWords);
			// ��ȡ��ҳ�е�����
			ArrayList<String> urls = getLink(content, new URL(startURL));
			// System.out.println(urls);
			int length = urls.size();
			// �������ȶ���
			for (String s : urls) {
				waitforHandling.add(new PriorityURL(s, cos / length));
				if (!isWaiting.contains(s))
					isWaiting.add(s);
				// System.out.println(s);
			}

			wanted.put(startURL, title);
			visitedURL = 1;
			insertWeb(title + "----" + startURL);
			label.setText("����������" + visitedURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void search(int threadNumber) {
		String url, content, title = "";
		Download d = new Download();
		while (visitedURL < urlCount && !stop) {
			if (waitforHandling.size() > 0) {
				url = waitforHandling.remove().getUrl();
				// System.out.println(url);
			} else
				break;

			try {
				content = d.downloadHttp(new URL(url));
				if (content != null) {
					Hashtable<String, Integer> destination = new Hashtable<String, Integer>();

					int count;
					// ͳ�ƹؼ��ʵĴ�Ƶ
					for (String key : keyWords.keySet()) {
						count = content.split(key).length - 1;
						destination.put(key, count);
					}

					// ������ض�
					double cos = calRelavancy(destination);
					synchronized (this) {
						if (cos < threshold) {
							noneRelevant.add(url);
							continue;// �����ض�С����ֵ������Ը���ҳ
						}
					}
					// ��ȡ��ҳ�е�����
					ArrayList<String> urls = getLink(content, new URL(url));
					int length = urls.size();
					// if (length > 80)
					// continue;
					// ��ȡtitle
					String regex = "<title>([^<]+)</title>";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(content);
					if (m.find())
						title = m.group(1);
					// �������ȶ���
					for (String s : urls) {
						// ���ʲ����޸ģ�����ͬ��
						if (wanted.containsKey(s)) {
							continue;
						}
						if (noneRelevant.contains(s)) {
							continue;
						}
						synchronized (this) {
							if (!isWaiting.contains(s)) {
								int numberofslash = url.split("/").length - 1;

								waitforHandling.add(new PriorityURL(s, cos / length / numberofslash));
								isWaiting.add(s);
							}
						}
					}
					wanted.put(url, title);
					insertWeb(title + ">>>>>>>>" + url);
					synchronized (this) {
						// �����Ǵ�����һ����ҳ֮�󣬸���ҳҪô��������Ҫ�ģ�Ҫô�ǵ�����ֵ�ģ�����ӵȴ�������ɾ��
						String temp = "";
						String x = GetContent.getcontent(content.toString(), temp);
						System.out.println(x);

						isWaiting.remove(url);
						visitedURL++;
						label.setText("����������" + visitedURL);
					}
				}
			} catch (MalformedURLException e) {
				// e.printStackTrace();
				continue;
			}
		}
		System.out.println("current thread num is " + --threadNum + ",waiting size is " + waitforHandling.size());
		synchronized (this) {
			wanted.clear();
			waitforHandling.clear();
			noneRelevant.clear();
			isWaiting.clear();
		}
	}

	private void insertWeb(String web) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setUnderline(set, true);
		try {
			textpane.getDocument().insertString(textpane.getDocument().getLength(), web + "\n", set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void stopSearch() {
		stop = true;
		synchronized (this) {
			wanted.clear();
			waitforHandling.clear();
			noneRelevant.clear();
			isWaiting.clear();
		}
	}

	/**
	 * ������ض�
	 * 
	 * @param destination
	 *            ��Ŀ����ҳ���ʼ��������ҳ������ض�
	 * @return
	 */
	public double calRelavancy(Hashtable<String, Integer> destination) {
		long sum1 = 0, sum2 = 0, sum3 = 0;
		for (String key : keyWords.keySet()) {
			sum1 += keyWords.get(key).intValue() * destination.get(key).intValue();
			sum2 += keyWords.get(key).intValue() * keyWords.get(key).intValue();
			sum3 += destination.get(key).intValue() * destination.get(key).intValue();
		}
		if (sum3 == 0)
			return 0;// ���һ����ҳ�����ǵ�����û�й�ϵ���п��ܻ�����0���������ڷ�ĸ��
		return sum1 * 1.0 / (Math.sqrt(sum2) * Math.sqrt(sum3));
	}

	/**
	 * ��ȡһ����ҳ������
	 * 
	 * @param content
	 *            �����ַ�������ȡ����
	 * @param url
	 *            ����Щ������Ĭ�ϵ�host������������Ҫ����content��Ӧ����ҳ
	 * @return
	 */
	public ArrayList<String> getLink(String content, URL url) {
		ArrayList<String> urls = new ArrayList<String>();
		String regex = "<a\\s*href=\"([^>\"]*)\"[^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		String s;
		while (m.find()) {
			s = m.group(1);
			if (s.length() == 1)
				continue;
			if (s.startsWith("/"))
				s = "http://" + url.getHost() + s;
			if (s.startsWith("http"))
				urls.add(s);
		}
		// System.out.println(urls);
		return urls;
	}

	public void writeToFile() {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("result"));
			System.out.println("wanted size is " + wanted.size());
			Enumeration<String> want = wanted.keys();
			while (want.hasMoreElements()) {
				writer.println("123" + (String) want.nextElement());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void parallelhandle() {
		for (int i = 0; i < threadCount; i++) {
			new Task(i).start();
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// ������һ���߳����жϣ�����������
		Runnable task = new Runnable() {
			public void run() {
				while (threadNum > 0)
					;
				button.setEnabled(true);
				JOptionPane.showMessageDialog(null, "Finish searching the pages!", "Done", JOptionPane.INFORMATION_MESSAGE);
			}
		};
		threadPool.execute(task);// ͨ������һ���µ��߳���ִ�н��ͳ���

	}

	/**
	 * ��ʾһ��������ڲ��࣬����ִ����ҳ����
	 * 
	 * @author ygch
	 * 
	 */
	class Task extends Thread {
		int number;

		Task(int number) {
			this.number = number;
			threadNum++;
		}

		public void run() {
			search(number);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Crawler crawler = new Crawler("�Ƽ���", "http://cloud.csdn.net/", new JTextPane(), new JLabel(), new JButton());
		crawler.addKeyWord("�Ƽ���", 0);
		crawler.addKeyWord("��������", 0);
		crawler.addKeyWord("ƽ̨", 0);
		crawler.addKeyWord("�ܹ�", 0);
		crawler.addKeyWord("���ݿ�", 0);
		crawler.addKeyWord("��ȫ", 0);
		crawler.addKeyWord("Hadoop", 0);
		crawler.addKeyWord("�洢", 0);
		crawler.addKeyWord("���⻯", 0);
		crawler.addKeyWord("��˽", 0);
		crawler.addKeyWord("�ڿ�", 0);
		crawler.addKeyWord("�ֲ�ʽ", 0);
		crawler.addKeyWord("MapReduce", 0);
		crawler.addKeyWord("cloud", 0);
		// crawler.addKeyWord("������", 0);
		crawler.initialize();
		crawler.parallelhandle();
		while (threadNum > 0)
			;
		crawler.writeToFile();
	}
}
