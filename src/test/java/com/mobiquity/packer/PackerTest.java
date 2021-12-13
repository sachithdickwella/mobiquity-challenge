package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class implementation for the class {@link Packer} including the
 * main functionality.
 *
 * @author Sachith Dickwella
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test 'com.mobiquity.packer.Packer' class")
class PackerTest {

    /**
     * Input file path with valid information for positive testing.
     */
    private static String inputFilePath;
    /**
     * Output file path with valid information for positive testing.
     */
    private static String outputSampleFilePath;
    /**
     * Input file path with invalid weight limit for negative testing.
     */
    private static String inputFileWithInvalidLimitPath;
    /**
     * Input file path with invalid weight limit - item list divide pattern.
     */
    private static String inputFileWithInvalidPatternPath;

    @BeforeAll
    static void setup() {
        inputFilePath = decode(
                requireNonNull(PackerTest.class.getResource("/input")).getFile(), UTF_8);
        outputSampleFilePath = decode(
                requireNonNull(PackerTest.class.getResource("/output")).getFile(), UTF_8);
        inputFileWithInvalidLimitPath = decode(
                requireNonNull(PackerTest.class.getResource("/input_with_invalid_limit"))
                        .getFile(), UTF_8);
        inputFileWithInvalidPatternPath = decode(
                requireNonNull(PackerTest.class.getResource("/input_with_invalid_pattern"))
                        .getFile(), UTF_8);
    }

    @Order(1)
    @DisplayName("On valid dataset 'com.mobiquity.packer.Packer#pack' success")
    @Test
    void testPositiveOpPacker() throws IOException {
        var packed = Packer.pack(inputFilePath);

        try (Stream<String> lines = Files.lines(new File(outputSampleFilePath).toPath())) {
            var output = lines.collect(Collectors.joining("\n"));
            assertEquals(output, packed);
        }
    }

    @Order(2)
    @DisplayName("On invalid weight limit, throw 'com.mobiquity.exception.APIException'")
    @Test
    void testInvalidWeightLimitOpPacker() {
        assertThrows(APIException.class,
                () -> Packer.pack(inputFileWithInvalidLimitPath),
                "Invalid params doesn't throw 'com.mobiquity.exception.APIException'");
    }

    @Order(3)
    @DisplayName("On invalid weight limit, item list break throw 'com.mobiquity.exception.APIException'")
    @Test
    void testInvalidPatternOpPacker() {
        assertThrows(APIException.class,
                () -> Packer.pack(inputFileWithInvalidPatternPath),
                "Invalid weight limit, item list break doesn't throw 'com.mobiquity.exception.APIException'");
    }

    @Order(4)
    @DisplayName("On invalid filepath, throw 'com.mobiquity.exception.APIException'")
    @Test
    void testInvalidFilePathOpPacker() {
        assertThrows(APIException.class,
                () -> Packer.pack("/invalid-file-path"),
                "Invalid file path doesn't throw 'com.mobiquity.exception.APIException'");
    }
}
