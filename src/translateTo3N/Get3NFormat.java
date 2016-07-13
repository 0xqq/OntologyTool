package translateTo3N;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class Get3NFormat {
	public static String N3_SUBJECT;
	public static String N3_PREDICATE;
	public static String N3_OBJECT;

	@SuppressWarnings("resource")
	public static String contentFromFile(String filename) throws IOException {
		OntModel om = ModelFactory.createOntologyModel();
		om.createClass("class1");
		om.createObjectProperty("property1");
		// om.write(System.out);
		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String temp = "";
		OntModel ontmodel1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		String NS = "http://OWLTEST/Things#";
		while ((temp = br.readLine()) != null) {
			System.out.println(temp);
			if (temp.contains("��") & temp.contains("��")) {

				String Subject = temp.substring(0, temp.indexOf("��"));
				System.out.print("\t����:" + Subject);
				// Resource s1 = ontmodel1.createResource(Subject);
				OntClass oc1 = ontmodel1.createClass(NS + Subject);

				String Property = temp.substring(temp.indexOf("��") + 1, temp.indexOf("��"));
				System.out.print("\t����:" + Property);
				// Property p = ontmodel1.createProperty(NS, Property);
				OntProperty op1 = ontmodel1.createObjectProperty(NS + Property);

				String Object = temp.substring(temp.indexOf("��") + 1, temp.length());
				System.out.println("\t����:" + Object);
				Resource s2 = ontmodel1.createResource(Object);
				// s1.addProperty(p, s2);

				Statement statement = ontmodel1.createStatement(oc1, op1, s2);
				// but remember to add the created statement to the model
				ontmodel1.add(statement);
				// ontmodel1.write(System.out);

			} else if (temp.contains("����")) {
				System.out.println("find it!!!");
				// Subject = temp.substring(0, temp.indexOf("��"));
				// Property = temp.substring(temp.indexOf("��"), temp.length());
				// Object = "��";

			} else if (temp.contains("��")) {
				if (temp.charAt(temp.indexOf("��") - 1) != '��') {
					System.out.println("���λ�ã�" + temp.indexOf("��"));
					System.out.println("��֮ǰ���ַ���" + temp.charAt(temp.indexOf("��") - 1));
				}
				// Subject ="!!"+ temp.substring(0, temp.indexOf("��"));
				// Property = temp.substring(temp.indexOf("��"), temp.length());
				// Object = "��";
			}
		}
		System.out.println(ontmodel1);
		ontmodel1.getWriter();
		return null;
	}

	public static String contentFromText(String temp) {
		String Subject = null;
		String Object = null;
		String Property = null;
		if (temp.contains("��") & temp.contains("��")) {
			Subject = temp.substring(0, temp.indexOf("��"));
			System.out.print("\t����:" + Subject);
			// Resource s1 = ontmodel1.createResource(Subject);
			// OntClass oc1 = ontmodel1.createClass(NS + Subject);

			Property = temp.substring(temp.indexOf("��") + 1, temp.indexOf("��"));
			System.out.print("\t����:" + Property);
			// Property p = ontmodel1.createProperty(NS, Property);
			// OntProperty op1 = ontmodel1.createObjectProperty(NS + Property);

			Object = temp.substring(temp.indexOf("��") + 1, temp.length());
			System.out.println("\t����:" + Object);
			// Resource s2 = ontmodel1.createResource(Object);
			// s1.addProperty(p, s2);

			// Statement statement = ontmodel1.createStatement(oc1, op1, s2);
			// but remember to add the created statement to the model
			// ontmodel1.add(statement);
			// ontmodel1.write(System.out);
			System.out.println(Subject + ":" + Property + ":" + Object);

		} else if (temp.contains("����")) {
			System.out.println("find it!!!");
			Subject = temp.substring(0, temp.indexOf("��"));
			Property = temp.substring(temp.indexOf("��"), temp.length());
			Object = "��";

		} else if (temp.contains("��")) {
			if (temp.charAt(temp.indexOf("��") - 1) != '��') {
				System.out.println("���λ�ã�" + temp.indexOf("��"));
				System.out.println("��֮ǰ���ַ���" + temp.charAt(temp.indexOf("��") - 1));
				Subject = temp.substring(0, temp.indexOf("��"));
				Property = temp.substring(temp.indexOf("��"), temp.length());
				Object = "��";
			}
		}
		if (Subject == null)
			return null;
		else
			return Subject + ":" + Property + ":" + Object;

	}
}
