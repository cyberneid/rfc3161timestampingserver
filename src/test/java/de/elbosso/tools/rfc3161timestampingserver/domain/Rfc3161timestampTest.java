package de.elbosso.tools.rfc3161timestampingserver.domain;

import de.elbosso.tools.rfc3161timestampingserver.Constants;
import de.elbosso.tools.rfc3161timestampingserver.dao.DaoFactory;
import de.elbosso.tools.rfc3161timestampingserver.dao.Rfc3161timestampDao;
import de.elbosso.tools.rfc3161timestampingserver.util.JpaDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;

public class Rfc3161timestampTest
{
	static DaoFactory df;

	@BeforeAll
	public static void setUp() throws ClassNotFoundException
	{
		df=new DaoFactory();
	}

    @Test
    @SetEnvironmentVariable(key = Constants.JDBC_URL, value = "jdbc:h2:mem:test")
    @SetEnvironmentVariable(key = Constants.JDBC_PASSWORD, value = "")
    @SetEnvironmentVariable(key = Constants.JDBC_USER, value = "sa")
    @SetEnvironmentVariable(key = Constants.PERSISTENCE_UNIT_NAME, value = Constants.PERSISTENCE_UNIT_NAME_FOR_TESTS)
    public void alwaysSuccess()
    {
	    Rfc3161timestampDao timestampDao=df.createRfc3161timestampDao();
		timestampDao.beginTransaction();
		Rfc3161timestamp timestamp=new Rfc3161timestamp();
		timestamp.setCreation_date(new Timestamp(java.util.Date.from(Instant.now()).getTime()));
		timestamp.setMessage_imprint_alg_oid("MD5");
		timestamp.setTsr_data("123456".getBytes(StandardCharsets.UTF_8));
		timestamp.setMessage_imprint_digest_base64("huhu");
		timestamp.setMessage_imprint_digest_hex("hallo");
		timestampDao.persist(timestamp);
		timestampDao.commitTransaction();
		java.math.BigDecimal id=timestamp.getId();
		Assertions.assertNotNull(id);
		Rfc3161timestamp timestampDB=timestampDao.find(timestamp.getId()).get();
	    Assertions.assertEquals(timestamp.getCreation_date(),timestampDB.getCreation_date());
	    Assertions.assertEquals(timestamp.getTsr_data(),timestampDB.getTsr_data());
	    Assertions.assertEquals(timestamp.getMessage_imprint_alg_oid(),timestampDB.getMessage_imprint_alg_oid());
	    Assertions.assertEquals(timestamp.getMessage_imprint_digest_base64(),timestampDB.getMessage_imprint_digest_base64());
	    Assertions.assertEquals(timestamp.getMessage_imprint_digest_hex(),timestampDB.getMessage_imprint_digest_hex());
    }

}
