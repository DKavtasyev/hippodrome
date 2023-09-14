import com.javarush.Hippodrome;
import com.javarush.Horse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование класса com.javarush.Hippodrome")
class HippodromeTest
{
	@Nested
	class CheckingHippodromeConstructor
	{
		@Test
		@DisplayName("Создание экземпляра класса com.javarush.Hippodrome с параметром, равным null")
		public void ifConstructorIsInvokedWithParamHavingValueNull_ThenExceptionMustBeThrown()
		{
			assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
		}

		@Test
		@DisplayName("Проверка сообщения в исключении при вызове конструктора с параметром null")
		public void ifConstructorIsInvokedWithParamHavingNullValue_ThenExceptionMessageMustBe()
		{
			Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
			assertEquals("Horses cannot be null.", exception.getMessage());
		}

		@Test
		@DisplayName("Проверка наличия исключения при передаче в конструктор класса com.javarush.Horse пустого списка лошадей")
		public void ifConstructorIsInvokedWithEmptyListAsParam_ThenExceptionMustBeThrown()
		{
			assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>()));
		}

		@Test
		@DisplayName("Проверка сообщения из исключения при передаче в конструктор класса com.javarush.Horse пустого списка лошадей")
		public void ifConstructorIsInvokedWithEmptyListAsParam_ThenExceptionMessageMustBe()
		{
			assertEquals("Horses cannot be empty.", assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>())).getMessage());
		}
	}

	@Nested
	@DisplayName("Тестирование метода getHorses()")
	class TestingHippodromeGetHorsesMethod
	{
		@Test
		@DisplayName("Проверка листа лошадей, возвращаемого методом getHorses()")
		public void whenGetHorsesMethodIsInvoked_ItMustReturnTheSameListWhichHaveBeenPassedToTheHippodromeConstructor()
		{
			List<Horse> horses = new ArrayList<>();
			for (int i = 0; i < 30; i++)
			{
				horses.add(new Horse("name" + i, 1 + i, 2 + i));
			}
			Hippodrome hippodrome = new Hippodrome(horses);
			Object[] array = hippodrome.getHorses().toArray();
//			array[1] = new com.javarush.Horse("name1", 2, 3);
			assertArrayEquals(horses.toArray(), array);
		}
	}

	@Nested
	@DisplayName("Тестирование метода move()")
	class TestingMoveMethodInHippodromeClass
	{
		@Test
		@DisplayName("Проверка работы метода move()")
		public void whenMoveMethodIsInvoked_ThenHorseMethodsMoveMustBeCallForEachHorse()
		{
			List<Horse> horses = new ArrayList<>();
			for (int i = 0; i < 50; i++)
				horses.add(Mockito.mock(Horse.class));
			Hippodrome hippodrome = new Hippodrome(horses);
			hippodrome.move();
			horses.forEach(horse -> Mockito.verify(horse).move());
		}
	}

	@Nested
	@DisplayName("Тестирование метода getWinner()")
	class TestingGetWinnerMethodInHippodromeClass
	{
		@Test
		@DisplayName("Проверка, что метод getWinner() возвращает лошадь, которая выиграла")
		public void whenGetWinnerIsCalled_ItMustReturnTheHorseHavingTheHighestDistanceValue()
		{
			List<Horse> horses = new ArrayList<>();
			Random random = new Random();
			for (int i = 0; i < 50; i++)
			{
				double distance = 49 * random.nextDouble() + 1;
				horses.add(new Horse("name" + i, i + 1, distance));
			}
			Hippodrome hippodrome = new Hippodrome(horses);
			@SuppressWarnings("all")
			Horse horse = horses.stream().max(Comparator.comparingDouble(Horse::getDistance)).get();
			assertSame(horse, hippodrome.getWinner());
		}
	}
}