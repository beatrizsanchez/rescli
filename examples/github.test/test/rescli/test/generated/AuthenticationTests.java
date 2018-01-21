package rescli.test.generated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.epsilonlabs.rescli.github.api.GitHubApi;
import org.epsilonlabs.rescli.github.api.IGitHubApi;
import org.epsilonlabs.rescli.test.github.query.GitHubTestUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AuthenticationTests extends GitHubTestUtil {

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AuthenticationTests.class);

	private static IGitHubApi api;

	@BeforeClass
	public static void setup(){
		GitHubTestUtil.setup();
	}
	
	@Before
	public void clearCache(){
		GitHubTestUtil.clearGitHubCache();
	}

	@Test
	public void testPublicSession() {
		api = GitHubApi.create()
				.setSession(publicSession)
				.build();
		api.getRate_limitRateLimit().observe().map(rate -> rate.getRate().getLimit()).blockingSingle();
		/*blockingSubscribe(rate -> {
			assertNotNull(rate);
			assertEquals(new Integer(60), rate.getRate().getLimit());
		});*/
	}

	@Test @Ignore
	public void testBasicSession() {
		api = GitHubApi.create()
				.setSession(basicSession)
				.build();

		api.getRate_limitRateLimit().observe().blockingSubscribe(rate -> {
			assertNotNull(rate);
			assertEquals(new Integer(5000), rate.getRate().getLimit());
		});
	}

	@Test @Ignore
	public void testOAuthSession() {
		api = GitHubApi.create()
				.setSession(basicSession)
				.build();

		api.getRate_limitRateLimit().observe().blockingSubscribe(rate -> {
			assertNotNull(rate);
			assertEquals(new Integer(5000), rate.getRate().getLimit());
		});
	}

}
