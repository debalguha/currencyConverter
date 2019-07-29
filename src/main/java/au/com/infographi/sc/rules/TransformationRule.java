package au.com.infographi.sc.rules;

import java.util.Map;
import java.util.function.Function;

public interface TransformationRule extends Function<Map<String, Object>, Map<String, Object>> {}
