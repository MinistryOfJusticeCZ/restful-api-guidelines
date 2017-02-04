package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class KebabCaseInPathSegmentsRuleTest {

    private final KebabCaseInPathSegmentsRule RULE = new KebabCaseInPathSegmentsRule();
    private final Swagger testSwagger = new Swagger();
    private final String testPath1 = "/shipment-order/{shipment_order_id}";
    private final String testPath2 = "/partner-order/{partner_order_id}";
    private final String testPath3 = "/partner-order/{partner_order_id}/partner-order/{partner_order_id}";
    private final String wrongTestPath1 = "/shipment_order/{shipment_order_id}";
    private final String wrongTestPath2 = "/partner-order/{partner_order_id}/partner-order1/{partner_order_id}";

    @Test
    public void validateEmptyPath(){
        assertThat(RULE.validate(testSwagger)).isEmpty();
    }

    @Test
    public void validateNormalPath(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(testPath1, new Path());
        testSwagger.paths(testData);
        assertThat(RULE.validate(testSwagger)).isEmpty();
    }

    @Test
    public void validateMultipleNormalPaths(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(testPath1, new Path());
        testData.put(testPath2, new Path());
        testData.put(testPath3, new Path());
        testSwagger.paths(testData);
        assertThat(RULE.validate(testSwagger)).isEmpty();
    }

    @Test
    public void validateFalsePath(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(wrongTestPath1, new Path());
        testSwagger.paths(testData);
        List<Violation> result = RULE.validate(testSwagger);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getPath().get()).isEqualTo(wrongTestPath1);
    }

    @Test
    public void validateMultipleFalsePaths(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(wrongTestPath1, new Path());
        testData.put(testPath2, new Path());
        testData.put(wrongTestPath2, new Path());
        testSwagger.paths(testData);
        List<Violation> result = RULE.validate(testSwagger);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(1).getPath().isPresent()).isTrue();
        assertThat(result.get(1).getPath().get()).isEqualTo(wrongTestPath1);
        assertThat(result.get(0).getPath().isPresent()).isTrue();
        assertThat(result.get(0).getPath().get()).isEqualTo(wrongTestPath2);
    }

}
