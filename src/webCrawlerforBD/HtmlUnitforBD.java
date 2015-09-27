package webCrawlerforBD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

///////////////������Դhttp://www.cnblogs.com/cation/p/3933408.html

/**
 * @author Zhao Hang
 * @date:2015-3-26����1:07:47
 * @email:1610227688@qq.com
 */
public class HtmlUnitforBD {
	private static int N = 100;// ��������
	private static String keyW = "android";// ������
	private static HtmlPage firstBaiduPage;// �����һҳ�������
	private static String format = "";// Baidu��Ӧÿ����������ĵ�һҳ�ڶ�ҳ����ҳ�ȵ����а�����&pn=1��,��&pn=2��,��&pn=3���ȵȣ���ȡ�����Ӳ�������Ի�ȡ��һ��ģ�壬���ڶ�λĳҳ�������
	private static ArrayList<String> eachurl = new ArrayList<String>();// ���ڱ�������

	public static void main(String[] args) throws Exception {
	}

	public static void mainFunction(final int n, final String keyWord, final JTextPane jtp, final JTextPane jtp2)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// �������ѯ����
				int page1linknum = 1;
				int x = n / 10;// ҳ��
				int y = n % 10;// �������
				System.out.println("Ҫ��ȡ�ٶȹ��ڡ�" + keyWord + "���������������" + N + "��" + "Ӧץȡ���������ǰ" + x + "ҳ�������" + y + "��Ϊ������ȡ�˴�����");
				// ��ȡ�������һҳ�ٶȲ�ѯ����
				Elements firstPageURL = null;
				try {
					firstPageURL = getFirstPage(keyWord);
				} catch (FailingHttpStatusCodeException | IOException e) {
					e.printStackTrace();
				}// ����firstPageURL��Ϊ��һ������ҳ��Ԫ�ؼ�
				for (Element newlink : firstPageURL) {
					String linkHref = newlink.attr("href");// ��ȡ������href����Ԫ�سɷ֣�JSoupʵ���ڲ��������
					String linkText = newlink.text();// �����������ڱ���ÿ�����ӵ�ժҪ
					if (linkHref.length() > 14 & linkText.length() > 2) {// ȥ��ĳЩ��Ч����
						System.out.println(linkHref + "\n\t\tժҪ��" + linkText);// ������Ӻ�ժҪ
						eachurl.add(linkHref);// ��Ϊ�洢�ֶδ洢��arrayList����
						insertWeb(jtp, page1linknum++ + ":" + linkHref);// �ڽ�������ʾ��������
						try {
							System.out.println("******" + linkHref);
							showWebInfo(jtp2, linkHref);
						} catch (FailingHttpStatusCodeException | BadLocationException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				nextHref(firstBaiduPage);// ��firstBaiduPage��Ϊ����������format������ҳ��ʽ��
				for (int i = 1; i < x; i++) {
					System.out.println("\n************�ٶ�������" + keyW + "����" + (i + 1) + "ҳ���************");
					String tempURL = format.replaceAll("&pn=1", "&pn=" + i + "");// ������֪��ʽ�޸������µ�һҳ������
					System.out.println(format.replaceAll("&pn=1", "&pn=" + i + ""));// ��ʾ������ģ��
					HtmlUnitforBD h = new HtmlUnitforBD();
					String htmls = h.getPageSource(tempURL, "utf-8");// ��֪Ϊ�δ˴�ֱ����JSoup����ش���ժȡ��ҳ���ݻ�������⣬���Բ����µı�����ʵ��ժȡ��ҳԴ��
					org.jsoup.nodes.Document doc = Jsoup.parse(htmls);// ��ҳ��Ϣת��Ϊjsoup��ʶ���docģʽ
					Elements links = doc.select("a[data-click]");// ժȡ��ҳ��������
					for (Element newlink : links) {// �ô�ͬ��getFirstPage�����ʵ��
						String linkHref = newlink.attr("href");
						String linkText = newlink.text();
						if (linkHref.length() > 14 & linkText.length() > 2) {// ɾ��ĳЩ��Ч���ӣ���鿴�ɷ�����Щ��Ч�����ǲ�������Ϣ�ı���
							System.out.println(linkHref + "\n\t\tժҪ��" + linkText);
							eachurl.add(linkHref);// ��Ϊ�洢�ֶδ洢��arrayList����
							insertWeb(jtp, page1linknum++ + ":" + linkHref);
							try {
								System.out.println("******" + linkHref);
								showWebInfo(jtp2, linkHref);
							} catch (FailingHttpStatusCodeException | BadLocationException | IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return;
			}
		});
		thread.start();
	}

	/*
	 * ��ȡ�ٶ�������һҳ����
	 */
	public static Elements getFirstPage(String w) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// ����Web Client
		String word = w;
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);// HtmlUnit��JavaScript��֧�ֲ��ã��ر�֮
		webClient.getOptions().setCssEnabled(false);// HtmlUnit��CSS��֧�ֲ��ã��ر�֮
		HtmlPage page = (HtmlPage) webClient.getPage("http://www.baidu.com/");// �ٶ�������ҳҳ��
		HtmlInput input = (HtmlInput) page.getHtmlElementById("kw");// ��ȡ����������ύ�������ݣ��鿴Դ���ȡԪ�����ƣ�
		input.setValueAttribute(word);// ��������ģ������ٶ������Ԫ��ID���ϣ�
		HtmlInput btn = (HtmlInput) page.getHtmlElementById("su");// ��ȡ������ť�����
		firstBaiduPage = btn.click();// ģ��������ť�¼�
		String WebString = firstBaiduPage.asXml().toString();// ����ȡ���İٶ������ĵ�һҳ��Ϣ���
		org.jsoup.nodes.Document doc = Jsoup.parse(WebString);// ת��ΪJsoupʶ���doc��ʽ
		System.out.println("************�ٶ�������" + word + "����һҳ���************");// �����һҳ���
		Elements links = doc.select("a[data-click]");// ���ذ�������<a......data-click=" "......>�ȵ�Ԫ�أ����JsoupAPI
		return links;// ���ش������ӣ�����һҳ�İٶ���������
	}

	/*
	 * ��ȡ��һҳ��ַ
	 */
	public static void nextHref(HtmlPage p) {
		// ���룺HtmlPage��ʽ��������һҳ����ҳ���ݣ�
		// �����format��ģ��
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		p = firstBaiduPage;
		String morelinks = p.getElementById("page").asXml();// ��ȡ���ٶȵ�һҳ�����ĵ׶˵�ҳ���html����
		org.jsoup.nodes.Document doc = Jsoup.parse(morelinks);// ת��ΪJsoupʶ���doc��ʽ
		Elements links = doc.select("a[href]");// ��ȡ���html�еİ���<a href=""....>�Ĳ���
		boolean getFormat = true;// ����ֻȡһ��ÿҳ���ӵ�ģ���ʽ
		for (Element newlink : links) {
			String linkHref = newlink.attr("href");// ����ȡ������<a>��ǩ�е�����ȡ��
			if (getFormat) {
				format = "http://www.baidu.com" + linkHref;// ��ȫģ���ʽ
				getFormat = false;
			}
		}
	}

	public String getPageSource(String pageUrl, String encoding) {
		// ���룺url����&�����ʽ
		// ���������ҳ����
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(pageUrl);// ����һURL����
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));// ʹ��openStream�õ�һ���������ɴ˹���һ��BufferedReader����
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			in.close();
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return sb.toString();
	}

	public static void insertWeb(JTextPane textpane, String web) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setUnderline(set, true);
		try {
			textpane.getDocument().insertString(textpane.getDocument().getLength(), web + "\n", set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public static void showWebInfo(final JTextPane textpane, final String weburl) throws FailingHttpStatusCodeException, MalformedURLException,
			BadLocationException, IOException {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				SimpleAttributeSet set = new SimpleAttributeSet();
				StyleConstants.setUnderline(set, true);
				String temp = null;
				try {
					textpane.getDocument().insertString(textpane.getDocument().getLength(), ">>>>>>"+transURLtoINFO.trans(weburl, temp) + "\n", set);
					System.out.println("#######################################################\n"+transURLtoINFO.trans(weburl, temp));
				} catch (BadLocationException | FailingHttpStatusCodeException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
}
