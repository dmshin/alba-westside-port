package dms.westside.portterr.alba;

import dms.westside.portterr.alba.dto.AlbaAddress;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

//@SpringBootTest
class AlbaApplicationTests {

//	@Test
//	void contextLoads() {
//	}


	@Test
	void testDoormanVerification() {
		AlbaAddress addr = new AlbaAddress();

		addr.setNotes("Doorman");
		Assert.isTrue(AlbaHelper.isDoorman(addr), "This was supposed to match but didn't: " + addr.getNotes());

		addr.setNotes("Doorman  ");
		Assert.isTrue(AlbaHelper.isDoorman(addr), "This was supposed to match but didn't: " + addr.getNotes());

		addr.setNotes("   Doorman");
		Assert.isTrue(AlbaHelper.isDoorman(addr), "This was supposed to match but didn't: " + addr.getNotes());

		addr.setNotes("slkfd Doorman sldfkj");
		Assert.isTrue(AlbaHelper.isDoorman(addr), "This was supposed to match but didn't: " + addr.getNotes());

		addr.setNotes("  Doorman  sldfkj");
		Assert.isTrue(AlbaHelper.isDoorman(addr), "This was supposed to match but didn't: " + addr.getNotes());

		addr.setNotes("slkfdj  DooRman  ");
		Assert.isTrue(AlbaHelper.isDoorman(addr), "This was supposed to match but didn't: " + addr.getNotes());

		addr.setNotes("slkfdj DooooRman  sldfkj");
		Assert.isTrue(!AlbaHelper.isDoorman(addr), "This was NOT supposed to match but did: " + addr.getNotes());

		addr.setNotes("Virtual Doorman");
		Assert.isTrue(!AlbaHelper.isDoorman(addr), "This was NOT supposed to match but did: " + addr.getNotes());

		addr.setNotes("slkfdj Virtual DoooRman  sldfkj");
		Assert.isTrue(!AlbaHelper.isDoorman(addr), "This was NOT supposed to match but did: " + addr.getNotes());

		addr.setNotes("Virtual DoooRman  sldfkj");
		Assert.isTrue(!AlbaHelper.isDoorman(addr), "This was NOT supposed to match but did: " + addr.getNotes());

		addr.setNotes("sdfdsVirtual DoooRman");
		Assert.isTrue(!AlbaHelper.isDoorman(addr), "This was NOT supposed to match but did: " + addr.getNotes());
	}

}
