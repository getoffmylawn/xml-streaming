import org.junit.Test;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class VtdTest {

	String xml = "<employees>"
			+ "     <employee emplid='1111' type='admin'>"
			+ "         <firstname>John</firstname>"
			+ "         <lastname>Watson</lastname>"
			+ "         <age>30</age>"
			+ "         <email>johnwatson@sh.com</email>"
			+ "     </employee>"
			+ "     <employee emplid='2222' type='admin'>"
			+ "         <firstname>Sherlock</firstname>"
			+ "         <lastname>Homes</lastname>"
			+ "         <age>32</age>"
			+ "         <email>sherlock@sh.com</email>"
			+ "     </employee>"
			+ "     <employee emplid='3333' type='user'>"
			+ "         <firstname>Jim</firstname>"
			+ "         <lastname>Moriarty</lastname>"
			+ "         <age>52</age>"
			+ "         <email>jim@sh.com</email>"
			+ "     </employee>"
			+ "     <employee emplid='4444' type='user'>"
			+ "         <firstname>Mycroft</firstname>"
			+ "         <lastname>Holmes</lastname>"
			+ "         <age>41</age>"
			+ "         <email>mycroft@sh.com</email>"
			+ "     </employee>"
			+ " </employees>";

	@Test
	public void testPerformance() throws Exception {
		String expression = "/employees/employee[@emplid='3333']/email/text()";
		VTDGen vg = new VTDGen();

		// runs in ~1.2 seconds
		for (int ii = 0; ii < 10000; ++ii) {
			vg.setDoc(xml.getBytes());
			vg.parse(false);

			VTDNav vn = vg.getNav();
			AutoPilot ap = new AutoPilot(vn);
			ap.selectXPath(expression);
			int result = -1;
			while((result = ap.evalXPath())!=-1){
				int t = vn.getText(); // get the index of the text (char data or CDATA)
				if (t!=-1) {
					vn.toNormalizedString(t);
				}
			}
		}
	}

}
