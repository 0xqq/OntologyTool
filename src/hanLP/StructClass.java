package hanLP;

import java.util.ArrayList;

/**
 * @author Zhao Hang
 * @date:2015-4-11����1:03:56
 * @email:1610227688@qq.com
 */
public class StructClass {

	public String ontObjectName;// ��������
	public String ontObjectParentName;// ��������
	public ArrayList<StructClassProperty> ontObjectProperty = new ArrayList<StructClassProperty>();// ����������б�
	public ArrayList<StructClassIndividual> ontObjectIndividual = new ArrayList<StructClassIndividual>();
}
