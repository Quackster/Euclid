package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleExtensionsTest {

    @Test
    void toClientValueFormatsOneDecimal() {
        assertEquals("0.0", DoubleExtensions.toClientValue(0.0));
        assertEquals("1.0", DoubleExtensions.toClientValue(1.0));
        assertEquals("3.1", DoubleExtensions.toClientValue(3.14159));
        assertEquals("-2.5", DoubleExtensions.toClientValue(-2.5));
        assertEquals("10.0", DoubleExtensions.toClientValue(10.0));
        assertEquals("9.9", DoubleExtensions.toClientValue(9.86));
    }
}
