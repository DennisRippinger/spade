/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade;

import info.interactivesystems.spade.calculation.UniqueReviewCrystalizer;
import info.interactivesystems.spade.recommender.HIndex;
import info.interactivesystems.spade.recommender.HIndexResolver;
import info.interactivesystems.spade.similarity.NilsimsaSimilarityCalculator;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.connector.Connector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * The Class CalculationApplication.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:beans.xml")
public class CalculationApplication {

    /**
     * The main method.
     * 
     * @param args the arguments
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {

        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(CliCommands.getCliOptions(), args);

        hIndex(args, line);
        hIndexResolver(args, line);
        nilsimsa(args, line);
        unique(args, line);
    }

    private static void unique(String[] args, CommandLine line) {
        ConfigurableApplicationContext context;
        if (line.hasOption(CliCommands.UNIQUE)) {
            log.info("Removing non-Unique Reviews");

            context = SpringApplication.run(CalculationApplication.class, args);
            UniqueReviewCrystalizer crystalizer = context.getBean(UniqueReviewCrystalizer.class);
            crystalizer.removeNonUnique();
        }
    }

    private static void hIndexResolver(String[] args, CommandLine line) {
        ConfigurableApplicationContext context;
        if (line.hasOption(CliCommands.HINDEXRESOLVER)) {
            String hIndexBoundary = line.getOptionValue(CliCommands.HINDEXRESOLVER);
            log.info("Finding Index Values greater than: '{}'", hIndexBoundary);

            Double boundary = Double.parseDouble(hIndexBoundary);

            context = SpringApplication.run(CalculationApplication.class, args);
            HIndexResolver resolver = context.getBean(HIndexResolver.class);
            resolver.resolveHIndex(boundary);
        }
    }

    private static void nilsimsa(String[] args, CommandLine line) {
        ConfigurableApplicationContext context;
        if (line.hasOption(CliCommands.NISLIMSA) && line.hasOption(CliCommands.CATEGORY)) {
            String category = line.getOptionValue(CliCommands.CATEGORY).replace("_", " ").replace("*", "&");
            log.info("CurrentCategory: '{}'", category);

            context = SpringApplication.run(CalculationApplication.class, args);
            NilsimsaSimilarityCalculator calculator = context.getBean(NilsimsaSimilarityCalculator.class);
            calculator.calculateSimilarityBetweenUniqueReviews(category);
        }
    }

    private static void hIndex(String[] args, CommandLine line) {
        ConfigurableApplicationContext context;
        if (line.hasOption(CliCommands.HINDEX) && line.hasOption(CliCommands.FROM) && line.hasOption(CliCommands.TO)) {
            String stringFrom = line.getOptionValue(CliCommands.FROM);
            String stringTo = line.getOptionValue(CliCommands.TO);

            try {
                Long from = Long.parseLong(stringFrom);
                Long to = Long.parseLong(stringTo);

                context = SpringApplication.run(CalculationApplication.class, args);
                HIndex hIndex = context.getBean(HIndex.class);

                hIndex.calculateHIndex(from, to);
            } catch (NumberFormatException e) {
                return;
            }

        }
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new MyCustomizer();
    }

    private static class MyCustomizer implements EmbeddedServletContainerCustomizer {

        @Override
        public void customize(ConfigurableEmbeddedServletContainer factory) {
            if (factory instanceof TomcatEmbeddedServletContainerFactory) {
                customizeTomcat((TomcatEmbeddedServletContainerFactory) factory);
            }
        }

        public void customizeTomcat(TomcatEmbeddedServletContainerFactory factory) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                @Override
                public void customize(Connector connector) {
                    connector.setPort(12000);
                }
            });
        }
    }
}
