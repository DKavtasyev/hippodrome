import com.javarush.Main;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MainTest
{
	@Test
	@DisplayName("Контроль времени выполнения программы")
	@Timeout(22)
	@Disabled
	public void verifyingThatTheMainMethodMustBeExecutedWithinASpecifiedTime() throws Exception
	{
		Main.main(new String[0]);
	}

}