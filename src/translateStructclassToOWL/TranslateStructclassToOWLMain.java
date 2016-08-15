package translateStructclassToOWL;

import hanLP.HanLPMain;
import hanLP._object;
import hanLP._objectproperty;
import ioOperation.WriteToFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import staticvariable.staticvalue;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

/*
 * ��������Ҫʹ����smi��protege3.5�����jar��
 */
public class TranslateStructclassToOWLMain {
	/**
	 * @param args
	 * @throws OntologyLoadException
	 * @throws IOException
	 */

	public static void main(String[] args) throws OntologyLoadException, IOException {
//		String s = "ֲ������������ǻ���������Ͷ����ߡ��ǻ�����������Ͷ������ǹ��ߡ����Ƕ�������������������һ���й��ˡ��¹���һ����Ȩ��֯��Eclipse��һ�����빤�ߣ����빤���ǹ����й������ˡ����������ˡ��°�����һ�������ˡ��¹���һ�����ҡ��°�����һ�������ˡ�";
//		String ss = "С�����ǳ���������С����һ����ʦ������ΰ���ǳ���ΰΰ������ΰ��һ����ʦ��С�����Ա����У�С����������27.С���ĸ�����������С����ĸ����������������һ����ʦ��������һ�����ˡ�С����һ����ʦ��С����һ���÷졣ֲ������������ǻ���������Ͷ����ߡ��ǻ�������������εĵ绰������13866655333���Ժ��ĵ绰������14787806414���Ժ���ѧ��֤������ɫ�Ǻ�ġ��Ժ���һ���й��ˡ�";
		String sss = "�������ݣ��������������񡣰��̲��Ĵ�����ǵ͵ġ������񡣰�֢���������Ǹߵġ���֢��Ǳ������5��ġ���֢��һ�����������̲���Ǳ������12�ꡣ���̲��ĸ�Ⱦ����ǿ�ġ����̲��ļ���������ʽ���Դ�����ëë��һ����ȸ����ĳ�������2����������ţţ��������10.ţţ��һ�����㣬��ȸ�ĳ�������2����ȸ����";
//		String ssss = "Eclipse��������ok�ġ���ð��Ǳ������2�졣��ð��һ��������ֲ������������񡣴�������ȵ�����񡣺ڳ�β����̨���Ұ�����ࡣ�������ë��ɫ�ǰ�ɫ�ġ������ë��ɫ�ǰ�ɫ��.С���޺���ͷ�����ʺ�ɫ������������Ŀ��������������������������ɫ�ģ��������й����Ҷ�����������Ĵ���ľݺ�������֡��̳����Ǳ�Σ���ࡣ����Ư���ĳ��";
		mainfunction(sss+"���������ĳ��", "base");
	}
	
	public static void writetotempFile(OWLModel o) throws IOException{
		String localaddr = staticvalue.localaddr;
		String filePathOut01 = localaddr + "/" + "tempowl.owl";
		FileOutputStream outFile = new FileOutputStream(filePathOut01);
		Writer out = new OutputStreamWriter(outFile, "UTF-8");
		OWLModelWriter omw = new OWLModelWriter(o, o.getTripleStoreModel().getActiveTripleStore(), out);
		omw.write();
		out.close();
	}
	
	public static boolean class_exist(OWLModel o, String class_) {
		boolean flag = false;
		OWLNamedClass on = o.getOWLNamedClass(class_);
		if (on == null)
			flag = false;
		else
			flag = true;
		return flag;
	}
	
	public static boolean individual_exist(OWLModel o, String individual_) {
		boolean flag = false;
		if(!class_exist(o, individual_)){
			OWLIndividual on = o.getOWLIndividual(individual_);
			if (on == null)
				flag = false;
			else
				flag = true;
		}
		return flag;
	}
	
	public static boolean property_exist(OWLModel o,String prop){
		boolean flag = false;
		OWLProperty op = o.getOWLProperty(prop);
		if (op == null){
			flag = false;
		}
		else
			flag = true;
		
		return flag;
	}

	public static boolean c_i_conflict(OWLModel o ,String instance){// new instance is conflicted with the class existing in ont
		boolean flag = false;
		OWLNamedClass on = o.getOWLNamedClass(instance);
		if (on == null)
			flag = false;
		else
			flag = true;
		return flag;
	}
	
	public static boolean i_c_conflict(OWLModel o ,String cls){// new class is conflicted with the instance existing in ont
		boolean flag = false;
		if (!class_exist(o, cls)){
			OWLIndividual on = o.getOWLIndividual(cls);
			if (on == null)
				flag = false;
			else
				flag = true;
		}
		return flag;
	}
	
	public static void donothing(){}
	
	public static OWLModel mainfunction(OWLModel o, String string) throws IOException {
		/*
		 * ���뱾�壬��һ����Ϊ�µ��ı�����ԭ����model�����ϼ�������µı���
		 * ���ν�������
		 */
		ArrayList<_object> _objlist = new ArrayList<_object>();
		HanLPMain.mainfunction(string);// �����ľ������ɵĽṹ���ำֵ��sc
		_objlist = HanLPMain.getStructclass();
		
		for (_object _o : _objlist) {
			int _object_type = _o.objecttype;
			String _object_name = _o.objectname;
			ArrayList<_object> _object_parent = _o.parent_object;
			ArrayList<_object> _object_sub = _o.sub_object;
			ArrayList<_objectproperty> _object_property = _o.objectproperty;

			boolean hasproperty = false;
			if (_object_property != null) {// if property exists in this ont
				hasproperty = true;
				for (_objectproperty _op : _object_property)
					if (!property_exist(o, _op.propertyname))
						o.createOWLObjectProperty(_op.propertyname);
			}
			if (_object_type == 0) {// if object is a class
				if (!class_exist(o, _object_name)) {// if class does't exists
					o.createOWLNamedClass(_object_name);// create this class
					if (_object_parent != null) {// if parent classes exist
						for (_object oparent : _object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectname);// add parent classes
							}
							if (!(oparent.objectname).equals("root")) {
								o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
								o.getOWLNamedClass(_object_name).removeSuperclass(o.getOWLThingClass());
							}
						}
					}
					if (hasproperty) {// if this class has properties
						for (_objectproperty oprop : _object_property) {// add
																		// properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
							o.getOWLNamedClass(_object_name).setPropertyValue(oop, oprop.propertyvalue);
						}
					}
					if (_object_sub != null) {// if subclasses exist
						for (_object osub : _object_sub) {// add sub classes
							if (osub.objecttype == 0) {// sub is class
								if (!class_exist(o, osub.objectname)) {// if this class not exist
									o.createOWLNamedClass(osub.objectname); // create this class
								}
								o.getOWLNamedClass(osub.objectname).addSuperclass(o.getOWLNamedClass(_object_name));
							}
							if (osub.objecttype == 1) {// sub is anindividual
								if (!individual_exist(o, osub.objectname) & !class_exist(o, osub.objectname)) {
									o.getOWLNamedClass(_object_name).createOWLIndividual(osub.objectname);
								}
							}
						}
					}
				} else if (class_exist(o, _object_name)) {// if class exists
					if (i_c_conflict(o, _object_name)) {// if this class does't have a instance-class conflict
						donothing();
					} else if (!i_c_conflict(o, _object_name)) {// if this class doesn't have a instance-class conflict
						if (_object_parent != null) {
							for (_object oparent : _object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectname);// add parent classes
								}
								if (!(oparent.objectname).equals("root")) {
									o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
									o.getOWLNamedClass(_object_name).removeSuperclass(o.getOWLThingClass());
								}
							}
						}
						if (hasproperty) {// if this class has properties
							for (_objectproperty oprop : _object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
								o.getOWLNamedClass(_object_name).setPropertyValue(oop, oprop.propertyvalue);
							}
						}
					}
				}
			}
			if (_object_type == 1) {// if object is an instance
				if (individual_exist(o, _object_name)) {// if this instance exists in ont'
					System.out.println("instance exists.");
					if (c_i_conflict(o, _object_name)) {// if this instance has a class-instance conflict
						@SuppressWarnings("unchecked")
						Collection<OWLNamedClass> onc = o.getOWLNamedClass(_object_name).getNamedSuperclasses();
						for (OWLNamedClass _onc : onc) {
							_onc.createOWLIndividual(_object_name);
						}
						@SuppressWarnings("rawtypes")
						Collection op = o.getOWLNamedClass(_object_name).getRDFProperties();
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+op);

						o.getOWLNamedClass(_object_name).delete();
						if (_object_parent != null) {
							for (_object oparent : _object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectname);// add parent classes
								}
								o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
							}
						}
						if (hasproperty) {// if this class has properties
							for (_objectproperty oprop : _object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
								o.getOWLIndividual(_object_name).setPropertyValue(oop, oprop.propertyvalue);
							}
						}
					}
				} else if (!individual_exist(o, _object_name)) {
					if (_object_parent != null) {
						for (_object oparent : _object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectname);// add parent classes
							}
							// o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
							o.getOWLNamedClass(oparent.objectname).createOWLIndividual(_object_name);
						}
					}
					if (hasproperty) {// if this class has properties
						for (_objectproperty oprop : _object_property) {// add properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
							o.getOWLIndividual(_object_name).setPropertyValue(oop, oprop.propertyvalue);
						}
					}
				}
			}
		}
		Format format = new SimpleDateFormat("yyMMdd-hhmmss");
		String time = format.format(new Date());
		staticvalue.tempfilename = time;
		// writetoFile(o);
		WriteToFile.writetoFile(o);
		return o;
	}

	public static OWLModel mainfunction(String string, String Resource) throws OntologyLoadException, IOException {
		/*
		 * �״ν��� string:�ı����� Resource:����ĸ��ڵ����ݣ��ı��ʻ���ʽ��
		 */
		OWLModel o = ProtegeOWL.createJenaOWLModel();
		o.getNamespaceManager().setDefaultNamespace("http://" + Resource + "#");

		ArrayList<_object> _objlist = new ArrayList<_object>();
		HanLPMain.mainfunction(string);// �����ľ������ɵĽṹ���ำֵ��sc
		_objlist = HanLPMain.getStructclass();

		for (_object _o : _objlist) {
			int _object_type = _o.objecttype;
			String _object_name = _o.objectname;
			ArrayList<_object> _object_parent = _o.parent_object;
			ArrayList<_object> _object_sub = _o.sub_object;
			ArrayList<_objectproperty> _object_property = _o.objectproperty;

			boolean hasproperty = false;
			if (_object_property != null) {// if property exists in this ont
				hasproperty = true;
				for (_objectproperty _op : _object_property)
					if (!property_exist(o, _op.propertyname))
						o.createOWLObjectProperty(_op.propertyname);
			}
			if (_object_type == 0) {// if object is a class
				if (!class_exist(o, _object_name)) {// if class does't exists
					o.createOWLNamedClass(_object_name);// create this class
					if (_object_parent != null) {// if parent classes exist
						for (_object oparent : _object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectname);// add parent classes
							}
							if (!(oparent.objectname).equals("root")) {
								o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
								o.getOWLNamedClass(_object_name).removeSuperclass(o.getOWLThingClass());
							}
						}
					}
					if (hasproperty) {// if this class has properties
						for (_objectproperty oprop : _object_property) {// add
																		// properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
							o.getOWLNamedClass(_object_name).setPropertyValue(oop, oprop.propertyvalue);
						}
					}
					if (_object_sub != null) {// if subclasses exist
						for (_object osub : _object_sub) {// add sub classes
							if (osub.objecttype == 0) {// sub is class
								if (!class_exist(o, osub.objectname)) {// if this class not exist
									o.createOWLNamedClass(osub.objectname); // create this class
								}
								o.getOWLNamedClass(osub.objectname).addSuperclass(o.getOWLNamedClass(_object_name));
							}
							if (osub.objecttype == 1) {// sub is anindividual
								if (!individual_exist(o, osub.objectname) & !class_exist(o, osub.objectname)) {
									o.getOWLNamedClass(_object_name).createOWLIndividual(osub.objectname);
								}
							}
						}
					}
				} else if (class_exist(o, _object_name)) {// if class exists
					if (i_c_conflict(o, _object_name)) {// if this class does't have a instance-class conflict
						donothing();
					} else if (!i_c_conflict(o, _object_name)) {// if this class doesn't have a instance-class conflict
						if (_object_parent != null) {
							for (_object oparent : _object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectname);// add parent classes
								}
								if (!(oparent.objectname).equals("root")) {
									o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
									o.getOWLNamedClass(_object_name).removeSuperclass(o.getOWLThingClass());
								}
							}
						}
						if (hasproperty) {// if this class has properties
							for (_objectproperty oprop : _object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
								o.getOWLNamedClass(_object_name).setPropertyValue(oop, oprop.propertyvalue);
							}
						}
					}
				}
			}
			if (_object_type == 1) {// if object is an instance
				if (individual_exist(o, _object_name)) {// if this instance exists in ont'
					System.out.println("instance exists.");
					if (c_i_conflict(o, _object_name)) {// if this instance has a class-instance conflict
						@SuppressWarnings("unchecked")
						Collection<OWLNamedClass> onc = o.getOWLNamedClass(_object_name).getNamedSuperclasses();
						for (OWLNamedClass _onc : onc) {
							_onc.createOWLIndividual(_object_name);
						}
						@SuppressWarnings("rawtypes")
						Collection op = o.getOWLNamedClass(_object_name).getRDFProperties();
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+op);

						o.getOWLNamedClass(_object_name).delete();
						if (_object_parent != null) {
							for (_object oparent : _object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectname);// add parent classes
								}
								o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
							}
						}
						if (hasproperty) {// if this class has properties
							for (_objectproperty oprop : _object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
								o.getOWLIndividual(_object_name).setPropertyValue(oop, oprop.propertyvalue);
							}
						}
					}
				} else if (!individual_exist(o, _object_name)) {
					if (_object_parent != null) {
						for (_object oparent : _object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectname)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectname);// add parent classes
							}
							// o.getOWLNamedClass(_object_name).addSuperclass(o.getOWLNamedClass(oparent.objectname));
							o.getOWLNamedClass(oparent.objectname).createOWLIndividual(_object_name);
						}
					}
					if (hasproperty) {// if this class has properties
						for (_objectproperty oprop : _object_property) {// add properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyname);
							o.getOWLIndividual(_object_name).setPropertyValue(oop, oprop.propertyvalue);
						}
					}
				}
			}
		}
		Format format = new SimpleDateFormat("yyMMdd-hhmmss");
		String time = format.format(new Date());
		staticvalue.tempfilename = time;
		// writetoFile(o);
		WriteToFile.writetoFile(o);
		return o;
	}
}