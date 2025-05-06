package dms.westside.portterr.alba;

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
		String address = "slkfdj>Doorman<sldfkj";
		Assert.isTrue(AlbaHelper.isDoorman(address), "This was supposed to match but didn't: " + address);

		address = "slkfdj>  Doorman  <sldfkj";
		Assert.isTrue(AlbaHelper.isDoorman(address), "This was supposed to match but didn't: " + address);

		address = "slkfdj>  DooRman  <sldfkj";
		Assert.isTrue(AlbaHelper.isDoorman(address), "This was supposed to match but didn't: " + address);

		address = "slkfdj DooRman  <sldfkj";
		Assert.isTrue(!AlbaHelper.isDoorman(address), "This was NOT supposed to match but did: " + address);

	}

}
