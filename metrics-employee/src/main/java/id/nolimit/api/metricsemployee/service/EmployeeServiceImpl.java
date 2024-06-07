package id.nolimit.api.metricsemployee.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static id.nolimit.api.metricsemployee.constant.ConstantValue.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmployeeServiceImpl implements EmployeeService {

    private final RestHighLevelClient client;
    private final SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
    private final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    public long getTotalEmployees() {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse.getHits().getTotalHits();
        } catch (IOException e) {
            log.error(e);
            return 0;
        }
    }

    public Double getAverageSalary() {
        try {
            AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg(AVERAGE_SALARY).field(SALARY);

            searchSourceBuilder.aggregation(avgAggregationBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            ParsedAvg averageSalary = searchResponse.getAggregations().get(AVERAGE_SALARY);
            return averageSalary.getValue();
        } catch (IOException e) {
            log.error(e);
            return 0D;
        }
    }

    public Map<String, Double> getMinimumAndMaximumSalary() {
        try {
            MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min(MINIMUM_SALARY).field(SALARY);
            MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max(MAXIMUM_SALARY).field(SALARY);

            searchSourceBuilder.aggregation(minAggregationBuilder);
            searchSourceBuilder.aggregation(maxAggregationBuilder);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            ParsedMin minSalary = searchResponse.getAggregations().get(MINIMUM_SALARY);
            ParsedMax maxSalary = searchResponse.getAggregations().get(MAXIMUM_SALARY);

            Map<String, Double> salaryStatistic = new HashMap<>();
            salaryStatistic.put(MINIMUM_SALARY, minSalary.getValue());
            salaryStatistic.put(MAXIMUM_SALARY, maxSalary.getValue());

            return salaryStatistic;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    public Map<Double, Long> getAgeDistribution() {
        try {
            HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram(AGE_DISTRIBUTION)
                    .field(AGE)
                    .interval(5);


            searchSourceBuilder.aggregation(histogramAggregationBuilder);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Histogram histogram = searchResponse.getAggregations().get(AGE_DISTRIBUTION);
            Map<Double, Long> ageDistribution = new HashMap<>();
            for (Histogram.Bucket bucket : histogram.getBuckets()) {
                ageDistribution.put(Double.valueOf(bucket.getKeyAsString()), bucket.getDocCount());
            }

            return ageDistribution;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    public Map<String, Long> getGenderDistribution() {
        try {
            searchSourceBuilder.aggregation(AggregationBuilders.terms(GENDER_DISTRIBUTION).field(GENDER));

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Terms genderTerms = searchResponse.getAggregations().get(GENDER_DISTRIBUTION);

            Map<String, Long> genderDistribution = new HashMap<>();
            for (Terms.Bucket bucket : genderTerms.getBuckets()) {
                genderDistribution.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            return genderDistribution;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    public Map<String, Long> getMaritalStatusDistribution() {
        try {
            searchSourceBuilder.aggregation(AggregationBuilders.terms(MARITAL_STATUS_DISTRIBUTION).field(MARITAL_STATUS));

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Terms maritalStatusTerms = searchResponse.getAggregations().get(MARITAL_STATUS_DISTRIBUTION);

            Map<String, Long> maritalStatusDistribution = new HashMap<>();
            for (Terms.Bucket bucket : maritalStatusTerms.getBuckets()) {
                maritalStatusDistribution.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            return maritalStatusDistribution;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    public Map<String, Long> getJoiningDateDistribution() {
        try {
            DateHistogramAggregationBuilder dateHistogramAggregationBuilder =
                    AggregationBuilders.dateHistogram(JOINING_DATE_DISTRIBUTION)
                            .field(DATE_OF_JOINING)
                            .dateHistogramInterval(DateHistogramInterval.MONTH);

            searchSourceBuilder.aggregation(dateHistogramAggregationBuilder);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Histogram dateHistogram = searchResponse.getAggregations().get(JOINING_DATE_DISTRIBUTION);

            Map<String, Long> joiningDateDistribution = new HashMap<>();
            for (Histogram.Bucket entry : dateHistogram.getBuckets()) {
                joiningDateDistribution.put(entry.getKeyAsString(), entry.getDocCount());
            }

            return joiningDateDistribution;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    public Map<String, Long> getCommonInterests() {
        try {
            searchSourceBuilder.aggregation(AggregationBuilders.terms(COMMON_INTERESTS).field(INTERESTS));

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Terms interestsTerms = searchResponse.getAggregations().get(COMMON_INTERESTS);

            Map<String, Long> commonInterests = new HashMap<>();
            for (Terms.Bucket entry : interestsTerms.getBuckets()) {
                commonInterests.put(entry.getKeyAsString(), entry.getDocCount());
            }

            return commonInterests;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    public Map<String, Long> getDesignationDistribution() {
        try {
            searchSourceBuilder.aggregation(AggregationBuilders.terms(DESIGNATION_DISTRIBUTION).field(DESIGNATIONS));

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Terms designationTerms = searchResponse.getAggregations().get(DESIGNATION_DISTRIBUTION);

            Map<String, Long> designationDistribution = new HashMap<>();
            for (Terms.Bucket entry : designationTerms.getBuckets()) {
                designationDistribution.put(entry.getKeyAsString(), entry.getDocCount());
            }

            return designationDistribution;
        } catch (IOException e) {
            log.error(e);
            return new HashMap<>();
        }
    }

}
