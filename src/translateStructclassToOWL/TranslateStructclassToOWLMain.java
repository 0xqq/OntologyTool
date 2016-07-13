package translateStructclassToOWL;

import hanLP.HanLPMain;
import hanLP.StructClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

/*
 * ��������Ҫʹ����smi��protege3.5�����jar��
 */
/**
 * @author Zhao Hang
 * @date:2015-5-2����2:55:23
 * @email:1610227688@qq.com
 */
public class TranslateStructclassToOWLMain {
	/**
	 * @param args
	 * @throws OntologyLoadException
	 * @throws IOException
	 */

	/*
	 * 
	 * ����������
	 * 
	 * 
	 * public static void main(String[] args) throws OntologyLoadException,
	 * IOException { String s =
	 * "ֲ������������ǻ���������Ͷ����ߡ��ǻ�����������Ͷ������ǹ��ߡ����Ƕ�������������������һ���й��ˡ��¹���һ����Ȩ��֯��Eclipse��һ�����빤�ߣ����빤���ǹ��ߡ����������л����񹲺͹�����ϯ,�й������ˡ����������ˡ���������һ���й��ˡ��°�����һ�������ˡ��¹���һ�����ҡ��°�����һ�������ˡ��л����񹲺͹���һ�����ҡ�"
	 * ; String ss =
	 * "�Ժ������ϴ�ѧ��ѧ����С�����ǳ���������С����һ����ʦ������ΰ���ǳ���ΰΰ������ΰ��һ����ʦ��С�����Ա����У�С����������27.С���ĸ�����������С����ĸ����������������һ����ʦ��������һ�����ˡ�С����һ����ʦ��С����һ���÷졣ֲ������������ǻ���������Ͷ����ߡ��ǻ�����������¹���һ����Ȩ��֯,�¹��ǹ��ҡ���ǿ����У��ѧ������ϯ�����εĵ绰������13866655333���Ժ��ĵ绰������14787806414���Ժ���ѧ��֤������ɫ�Ǻ�ġ��Ժ���һ���й��ˡ�"
	 * ; String sss =
	 * "�����񡣰�֢���������Ǹߵġ���֢��Ǳ������5��ġ���֢��һ�����������̲���Ǳ������12�ꡣ���̲��ĸ�Ⱦ����ǿ�ġ����̲��ļ���������ʽ���Դ��������̲���һ��������ëë����ë��ɫ�Ǻ�ɫ�ġ�ëë��һ����ȸ����ĳ�������2����������ţţ��������10.�������ë��ɫ�ǰ�ɫ�ġ������ë��ɫ�ǰ�ɫ�ġ�ţţ��һ�����㣬��ȸ�ĳ�������2����ȸ����"
	 * ; String ssss = "Eclipse��������ok�ġ���ð��Ǳ������2�졣��ð��һ��������"; String sssss = "";
	 * String fileuri = "D:/owlmodel/aaaaaaaa.owl"; mainfunction(s + ss + sss +
	 * ssss + sssss, fileuri, "base", "kkkkkkk"); }
	 */
	
	/*
	 * Я�� ����
	 */
	public static void writetoFile(String s, OWLModel o) throws IOException {
		
		String filePathOut01 = "D:/owlmodel/" + s + ".owl";
		// д�룺
		FileOutputStream outFile = new FileOutputStream(filePathOut01);
		Writer out = new OutputStreamWriter(outFile, "UTF-8");
		OWLModelWriter omw = new OWLModelWriter(o, o.getTripleStoreModel().getActiveTripleStore(), out);
		omw.write();
		out.close();
	}

	public static OWLModel mainfunction(String string, String fileuri, String Resource, String filename) throws OntologyLoadException, IOException {
		OWLModel o = ProtegeOWL.createJenaOWLModel();

		o.getNamespaceManager().setDefaultNamespace("http://" + Resource + "#");
		ArrayList<StructClass> sc = new ArrayList<StructClass>();// �½�ArrayList׼�������string�л�ȡ�����Ѵ���ı�
		sc = HanLPMain.mainfunction(string);// �����ľ������ɵĽṹ���ำֵ��sc
		int scClassSize = sc.size();

		while (scClassSize-- > 0) {
			System.out.println(scClassSize + ":");
			String classname = sc.get(scClassSize).ontObjectName;
			String parentclassname = sc.get(scClassSize).ontObjectParentName;
			int classpropertynumber = sc.get(scClassSize).ontObjectProperty.size();
			int classindividualnumber = sc.get(scClassSize).ontObjectIndividual.size();
			if (!parentclassname.equals("root") & parentclassname != null) {// �����Խ�������ӽ�����
				tryToAddClassIntoOWLModel(o, classname);
				// �����Խ�������ӽ�����
				tryToAddClassIntoOWLModel(o, parentclassname);
				// �����Խ�����͸��๹��һ��������Ĺ�ϵ
				tryToAddSubclassIntoOWLModel(o, classname, parentclassname);
			}
			int scPropertySize = classpropertynumber;
			int scIndividualSize = classindividualnumber;
			while (scIndividualSize-- > 0) {
				String individualname = sc.get(scClassSize).ontObjectIndividual.get(scIndividualSize).ontObjectIndividualName;
				System.out.println("ʵ������Ϊ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + individualname);
				tryToAddObjectIndividualIntoOWLModel(o, classname, individualname);
			}
			while (scPropertySize-- > 0) {
				// ���Խ����������ӽ�ģ��
				String propertyname = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyName;
				String propertyvalue = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyValue;
				tryToAddObjectPropertyIntoOWLModel(o, propertyname);
				attachPropertyTOClassOrIndividual(o, classname, propertyname, propertyvalue);
			}
		}
		writetoFile(filename, o);
		return o;
	}

	public static OWLModel mainfunction(OWLModel owlModel, String info, String OwlOntBaseURI, String writetoFileName) throws OntologyLoadException,
			IOException {
		// Ҫ���ĵ�ģ��
		// �������Ϣ
		// ��Ŀ¼
		// ������ļ���
		OWLModel o = ProtegeOWL.createJenaOWLModel();
		o = owlModel;

		o.getNamespaceManager().setDefaultNamespace("http://" + OwlOntBaseURI + "#");
		ArrayList<StructClass> sc = new ArrayList<StructClass>();// �½�ArrayList׼�������string�л�ȡ�����Ѵ���ı�
		sc = HanLPMain.mainfunction(info);// �����ľ������ɵĽṹ���ำֵ��sc
		int scClassSize = sc.size();

		while (scClassSize-- > 0) {
			System.out.println(scClassSize + ":");
			String classname = sc.get(scClassSize).ontObjectName;
			String parentclassname = sc.get(scClassSize).ontObjectParentName;
			int classpropertynumber = sc.get(scClassSize).ontObjectProperty.size();
			int classindividualnumber = sc.get(scClassSize).ontObjectIndividual.size();
			tryToAddClassIntoOWLModel(o, classname);
			if (!parentclassname.equals("root") & parentclassname != null) {
				tryToAddClassIntoOWLModel(o, parentclassname);
				tryToAddSubclassIntoOWLModel(o, classname, parentclassname);
			}
			int scPropertySize = classpropertynumber;
			int scIndividualSize = classindividualnumber;
			while (scIndividualSize-- > 0) {
				String individualname = sc.get(scClassSize).ontObjectIndividual.get(scIndividualSize).ontObjectIndividualName;
				System.out.println("ʵ������Ϊ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + individualname);
				tryToAddObjectIndividualIntoOWLModel(o, classname, individualname);
			}
			while (scPropertySize-- > 0) {
				// ���Խ����������ӽ�ģ��
				String propertyname = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyName;
				String propertyvalue = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyValue;
				tryToAddObjectPropertyIntoOWLModel(o, propertyname);
				attachPropertyTOClassOrIndividual(o, classname, propertyname, propertyvalue);
			}
		}
		owlModel = o;
		writetoFile(writetoFileName, owlModel);
		return owlModel;
	}

	/*
	 * ���������Ĳ���
	 */
	public static void tryToAddClassIntoOWLModel(OWLModel o, String s) {
		try {
			if (!checkClass(o, s)) {
				System.out.println("�޴���" + s);
				operatorforClass(o, s);
			} else {
				System.out.println("�д���" + s);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean checkClass(OWLModel o, String s) {
		boolean flag = false;
		Collection classes = o.getUserDefinedOWLNamedClasses();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			System.out.println("^^^^^^^^^^^^^" + cls.getBrowserText());
			if (cls.getBrowserText().equals(s)) {
				flag = true;
				break;
			}
		}
		Collection classes2 = o.getUserDefinedRDFSNamedClasses();
		for (Iterator it = classes2.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			if (cls.getBrowserText().equals(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@SuppressWarnings("unused")
	public static void operatorforClass(OWLModel o, String s) {
		OWLNamedClass tempclass;
		if (!checkIfClassIsAnIndividual(o, s)) {
			if (!checkClass(o, s)) {
				if (!checkIndividual(o, s)) {
					System.out.println("start to add class" + s);
					tempclass = o.createOWLNamedClass(s);
				}
			}
		}
	}

	/*
	 * �������
	 */
	private static void tryToAddSubclassIntoOWLModel(OWLModel o, String subclassName, String parentClassName) {
		if (checkClass(o, parentClassName) & !checkIndividual(o, subclassName)) {
			System.out.println("�д� ����");
			operatorforSubclass(o, subclassName, parentClassName);
		} else {
			System.out.println("�޴˸���");
		}
	}

	private static void operatorforSubclass(OWLModel o, String subclassName, String parentClassName) {
		o.getOWLNamedClass(subclassName).addSuperclass(o.getOWLNamedClass(parentClassName));
		o.getOWLNamedClass(subclassName).removeSuperclass(o.getOWLThingClass());
	}

	/*
	 * �ж�һ�����Ƿ�Ϊ����
	 */
	private static boolean checkIfClassIsAnIndividual(OWLModel o, String classname) {
		boolean flag = checkIndividual(o, classname);
		return flag;
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkIndividual(OWLModel o, String classname) {
		boolean flag = false;
		Collection classes = o.getOWLIndividuals();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLIndividual oi = (OWLIndividual) it.next();
			if (oi.getBrowserText().equals(classname)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private static void tryToAddObjectIndividualIntoOWLModel(OWLModel o, String classname, String individualname) {
		tryToAddClassIntoOWLModel(o, classname);
		System.out.println("׼����ӡ�" + individualname + "���������");
		if (!checkClass(o, individualname)) {
			System.out.println("��ǰ�������Ը�������" + individualname + "���������");
			if (!checkIfIndividualExistsInModel(o, individualname))
				if (!checkIfIndividualIsAClass(o, individualname))
					tryToAddIndividualIntoOWLNamedClass(o, classname, individualname);
		}

	}

	private static boolean checkIfIndividualIsAClass(OWLModel o, String individualname) {
		// ����K����true�h��
		// �����������Ƿ���һ���࣬���ǣ�����true
		boolean flag = checkClass(o, individualname);
		// �������Ƿ������������壬�����ڷ���true
		flag = checkIfIndividualExistsInModel(o, individualname);
		return flag;
	}

	@SuppressWarnings("unused")
	private static void tryToAddIndividualIntoOWLNamedClass(OWLModel o, String classname, String individualname) {
		OWLIndividual oi = o.getOWLNamedClass(classname).createOWLIndividual(individualname);
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkIfIndividualExistsInModel(OWLModel o, String individualname) {

		boolean flag = false;

		Collection classes = o.getUserDefinedOWLNamedClasses();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			Collection instances = cls.getInstances(true);
			for (Iterator it2 = instances.iterator(); it2.hasNext();) {
				OWLIndividual oi = (OWLIndividual) it2.next();
				if (oi.getBrowserText().equals(individualname)) {
					flag = true;
					break;

				}
			}
		}
		return flag;
	}

	/*
	 * ������ԵĲ���
	 */
	private static void tryToAddObjectPropertyIntoOWLModel(OWLModel o, String s) {
		if (!checkProperty(o, s)) {
			if (!checkClass(o, s)) {
				System.out.println("�޴� ����");
				operatorforProperty(o, s);
			}
		} else {
			System.out.println("�д�����");
		}
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkProperty(OWLModel o, String s) {
		boolean flag = false;
		Collection c1 = o.getUserDefinedOWLDatatypeProperties();
		for (Iterator it = c1.iterator(); it.hasNext();) {
			try {
				OWLObjectProperty oop = (OWLObjectProperty) it.next();
				if (oop.getBrowserText().equals(s)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
			}
		}

		Collection c2 = o.getUserDefinedRDFProperties();
		for (Iterator it = c2.iterator(); it.hasNext();) {
			try {
				OWLObjectProperty oop = (OWLObjectProperty) it.next();
				if (oop.getBrowserText().equals(s)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
			}
		}
		Collection c3 = o.getUserDefinedOWLObjectProperties();
		for (Iterator it = c3.iterator(); it.hasNext();) {
			try {
				OWLObjectProperty oop = (OWLObjectProperty) it.next();
				if (oop.getBrowserText().equals(s)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
			}
		}
		return flag;
	}

	private static void operatorforProperty(OWLModel o, String s) {
		System.out.println("start to add");
		@SuppressWarnings("unused")
		OWLObjectProperty oop = o.createOWLObjectProperty(s);
	}

	// public static boolean checkIndividualInClass(OWLModel o, String
	// classname, String individualname) {
	// boolean flag = false;
	// Collection classes = o.getUserDefinedOWLNamedClasses();
	// for (Iterator it = classes.iterator(); it.hasNext();) {
	// OWLNamedClass cls = (OWLNamedClass) it.next();
	// if (cls.getBrowserText().equals(classname)) {
	// Collection instances = cls.getInstances(true);
	// for (Iterator it2 = instances.iterator(); it2.hasNext();) {
	// OWLIndividual oi = (OWLIndividual) it2.next();
	// if (oi.getBrowserText().equals(individualname)) {
	// flag = true;
	// break;
	// }
	// }
	// }
	// }
	// return flag;
	// }

	private static void attachPropertyTOClassOrIndividual(OWLModel o, String classname, String propertyname, String propertyvalue) {
		tryToAddObjectPropertyIntoOWLModel(o, propertyname);
		tryToAddClassIntoOWLModel(o, classname);
		System.out.println("������Գɹ�");
		OWLObjectProperty oop = o.getOWLObjectProperty(propertyname);

		if (checkClass(o, classname)) {
			System.out.println("##########################��������");
			oop.addUnionDomainClass(o.getOWLNamedClass(classname));
		}
		if (checkIndividual(o, classname)) {
			System.out.println("##########################����������");
			o.getOWLIndividual(classname).setPropertyValue(oop, propertyvalue);
		}
	}
}