import com.javarush.Horse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование класса com.javarush.Horse")
class HorseTest
{
	@Nested
	@DisplayName("Тестирование проверки параметров конструктора класса com.javarush.Horse")
	class CheckingConstructorParameters
	{
		@Nested
		@DisplayName("Создание лошади с передачей null вместо имени")
		class CreatingTheHorseWithSettingNullAsNameParameter
		{
			Throwable exception;

			@BeforeEach
			public void init()
			{
				exception = assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1, 1));
			}

			@Test
			@DisplayName("Проверка сообщения в исключении при передаче null в первом параметре com.javarush.Horse")
			public void whenFirstParamOfHorseConstructorIsNull_WhetherExceptionIsThrown()
			{
				assertAll(() -> {
					assertEquals("java.lang.IllegalArgumentException", exception.getClass().getName());
					assertEquals("Name cannot be null.", exception.getMessage());

				});
			}
		}

		@Nested
		@DisplayName("Создание лошади с передачей бланк-символа вмести имени")
		class CreatingTheHorseWithSettingBlankAsNameParameter
		{
			Throwable exception;

			@BeforeEach
			@ParameterizedTest
			@MethodSource("argsProvidedFactory")
			public void init()
			{
				exception = assertThrows(IllegalArgumentException.class, () -> new Horse("", 1, 1));
			}

			static Stream<String> argsProvidedFactory()
			{
				return Stream.of("", " ", "\t", "\n", "\u00A0", "\u2007", "\u202F", "\u000B", "\f", "\r", "\u001C", "\u001D", "\u001E", "\u001F");
			}

			@Test
			@DisplayName("Наличие исключения при передаче blank-символа в первом параметре com.javarush.Horse")
			public void someTest()
			{
				assertAll(() -> {
					assertEquals("java.lang.IllegalArgumentException", exception.getClass().getName());
					assertEquals("Name cannot be blank.", exception.getMessage());

				});
			}
		}

		@Nested
		@DisplayName("Создание лошади с передачей отрицательного числа в качестве значения скорости")
		class CreatingTheHorseWithNegativeDigitsAsHorseSpeed
		{
			Throwable exception;

			@BeforeEach
			public void init()
			{
				exception = assertThrows(IllegalArgumentException.class, () -> new Horse("name", -1, 1));
			}

			@Test
			public void whenTheSecondParamOfHorseConstructorIsNegative_WhetherExceptionIsThrown()
			{
				assertAll(() -> {
					assertEquals("java.lang.IllegalArgumentException", exception.getClass().getName());
					assertEquals("Speed cannot be negative.", exception.getMessage());
				});
			}
		}

		@Nested
		@DisplayName("Создание лошади с передачей отрицательного числа в качестве значения дистанции")
		class CreatingTheHorseWithNegativeDigitsAsHorseDistance
		{
			Throwable exception;

			@BeforeEach
			public void init()
			{
				exception = assertThrows(IllegalArgumentException.class, () -> new Horse("name", 1, -1));
			}

			@Test
			public void whenTheThirdParamOfHorseConstructorIsNegative_WhetherExceptionIsThrown()
			{
				assertAll(() -> {
					assertEquals("java.lang.IllegalArgumentException", exception.getClass().getName());
					assertEquals("Distance cannot be negative.", exception.getMessage());
				});
			}
		}
	}

	@Nested
	@DisplayName("Тестирование геттеров класса com.javarush.Horse")
	class TestingHorseGetterMethods
	{
		@DisplayName("Тестирование метода getName() класса com.javarush.Horse")
		@ParameterizedTest
		@ValueSource(strings = {"name",
				"}QarU~x48|WX#eq}A8QOFwK2QJ6Wvw|cY#x@w?5ycX~p2Cc@A55W*hp{HueNe3PR",
				"6G2bItQK@u@r1|mNKF5rNeHept5p$4p04e1Bv$2#gVqgmX8W|VPvuHEj5d%2Uk7J"})
		public void willTheNameBeReturnedByGetNameMethod(String argument)
		{
			Horse horse = new Horse(argument, 1, 1);
			assertEquals(argument, horse.getName());
		}

		@DisplayName("Тестирование метода getSpeed() класса com.javarush.Horse")
		@ParameterizedTest
		@ValueSource(doubles = {0.000_000_001, 0.5, Double.MAX_VALUE})
		public void willTheSpeedValueBeReturnedByGetSpeedMethod(double value)
		{
			Horse horse = new Horse("name", value, 1);
			assertEquals(value, horse.getSpeed());
		}

		@Nested
		@DisplayName("Тестирование метода getDistanse() класса com.javarush.Horse")
		class TestingMethodGetDistance
		{
			@ParameterizedTest
			@DisplayName("Тестирование метода getDistance() класса com.javarush.Horse на корректность возвращаемого значения")
			@ValueSource(doubles = {0.000_001, 50, Double.MAX_VALUE})
			public void willTheDistanceValueBeReturnedByGetDistanceMethod(double value)
			{
				Horse horse = new Horse("name", 1, value);
				assertEquals(value, horse.getDistance());
			}

			@Test
			@DisplayName("Тестирование метода getDistance() класса com.javarush.Horse при вызове конструктора com.javarush.Horse с двумя параметрами")
			public void willTheGetDistanceMethodReturnZeroIfHorseConstructorHasBeenInvokedWithTwoParameters()
			{
				Horse horse = new Horse("name", 1);
				assertThat(horse.getDistance(), equalTo(0.0));
			}
		}


	}

	@Nested
	@ExtendWith(MockitoExtension.class)
	@DisplayName("Тестирование метода move()")
	class TestingHorseMethodMove
	{
		@Test
		@DisplayName("Проверка, что метод move() вызывает метод getRandomValue с параметрами 0.2, 0.9")
		public void whenCallingMoveThenMustBeCalledGetRandomDoubleWithDefinedParameters()
		{
			Horse horse = new Horse("name", 1, 1);
			try (MockedStatic<Horse> utilities = Mockito.mockStatic(Horse.class))
			{
				horse.move();
				utilities.verify(() -> Horse.getRandomDouble(0.2, 0.9));
			}
		}

		@ParameterizedTest
		@ValueSource(doubles = {0.2, 0.5, 0.89999999})
		@DisplayName("Проверка изменения дистанции лошади методом move()")
		public void whenMoveMethodIsCalledThenHeMustAssignTheDistanceValue(double randomDouble)
		{
			try (MockedStatic<Horse> utilities = Mockito.mockStatic(Horse.class))
			{
				utilities.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(randomDouble * (0.9 - 0.2) + 0.2);
				Horse horse = new Horse("name", 1, 1);
				horse.move();
				assertThat(horse.getDistance(), equalTo(1 + 1 * (randomDouble * (0.9 - 0.2) + 0.2)));
			}
		}
	}
}