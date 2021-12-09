package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import org.junit.jupiter.api.*;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * @author Sachith Dickwella
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test 'com.mobiquity.packer.Packer' class")
class PackerTest {

    private static String inputFilePath;

    private static String outputSampleFilePath;

    @BeforeAll
    static void setup() {
        inputFilePath = decode(requireNonNull(PackerTest.class.getResource("/example_input")).getFile(), UTF_8);
        outputSampleFilePath = decode(requireNonNull(PackerTest.class.getResource("/example_output")).getFile(), UTF_8);

    }

    @Order(1)
    @DisplayName("'com.mobiquity.packer.Packer#pack' return valid results")
    @Test
    void testPositiveOpPacker() throws APIException {
        var s = Packer.pack(inputFilePath);
    }


}
