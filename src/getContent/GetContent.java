package getContent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhao Hang
 * @date:2015-5-26����1:05:37
 * @email:1610227688@qq.com ��
 */
public class GetContent {
	private static final String regEx_all = "<script[^>]*?>[\\s\\S]*?<\\/script>|<style[^>]*?>[\\s\\S]*?<\\/style>|<[^>]+>|\\s*|\t|\r|\n|\\-";
	// �ֱ�����script��style��html���ո��е�������ʽ
	static ArrayList<String> hrefList = new ArrayList<String>();

	/*
	 * �������
	 */
	public static String getcontent(String webcontent, String newcontent) {
		if (webcontent != null) {
			newcontent = delHTMLTag(newcontent);
			System.out.println(newcontent);
			if (newcontent != null)
				hrefList.add(newcontent);
		}
		String string = null;
		for (int i = 0; i < hrefList.size(); i++) {
			string = getTextFromHtml(hrefList.get(i));
			if (string.length() >= 14)
				System.out.println("------" + i + ":" + string);
		}
		return newcontent;
	}

	public static String getTextFromHtml(String htmlStr) {
		delHTMLTag(htmlStr).replaceAll("&nbsp;", "");
		return htmlStr;
	}

	public static String delHTMLTag(String htmlStr) {
		Pattern p_all = Pattern.compile(regEx_all, Pattern.CASE_INSENSITIVE);
		Matcher m_all = p_all.matcher(regEx_all);
		htmlStr = m_all.replaceAll("");
		return htmlStr.trim();
	}
}

/*
 * ƥ������htmlԪ��
 */
// // ����script��������ʽ
// private static final String regEx_script =
// "<script[^>]*?>[\\s\\S]*?<\\/script>";
// // ����style��������ʽ
// private static final String regEx_style =
// "<style[^>]*?>[\\s\\S]*?<\\/style>";
// // ����HTML��ǩ��������ʽ
// private static final String regEx_html = "<[^>]+>";
// // ����ո�س����з�
// private static final String regEx_space = "\\s*|\t|\r|\n|\\-";
// // ���ڱ������л�õ����Ӳ�����

//
// Pattern p_space = Pattern.compile(regEx_space,
// Pattern.CASE_INSENSITIVE);
// Matcher m_space = p_space.matcher(htmlStr);
// htmlStr = m_space.replaceAll(""); // ���˿ո�س���ǩ
//
// Pattern p_script = Pattern.compile(regEx_script,
// Pattern.CASE_INSENSITIVE);
// Matcher m_script = p_script.matcher(htmlStr);
// htmlStr = m_script.replaceAll(""); // ����script��ǩ
//
// Pattern p_style = Pattern.compile(regEx_style,
// Pattern.CASE_INSENSITIVE);
// Matcher m_style = p_style.matcher(htmlStr);
// htmlStr = m_style.replaceAll(""); // ����style��ǩ
//
// Pattern p_html = Pattern.compile(regEx_html,
// Pattern.CASE_INSENSITIVE);
// Matcher m_html = p_html.matcher(htmlStr);
// htmlStr = m_html.replaceAll(""); // ����html��ǩ

