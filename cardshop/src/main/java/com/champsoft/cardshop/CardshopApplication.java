package com.champsoft.cardshop;

import com.champsoft.cardshop.DataAccessLayer.Entities.Card;
import com.champsoft.cardshop.DataAccessLayer.Repositories.CardRepository;
import com.champsoft.cardshop.DataAccessLayer.Entities.Collector;
import com.champsoft.cardshop.DataAccessLayer.Repositories.CollectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class CardshopApplication {

	private static final Logger log = LoggerFactory.getLogger(CardshopApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CardshopApplication.class, args);
	}

	@Bean
	CommandLineRunner seed(CollectorRepository collectors, CardRepository cards) {
		return args -> {
			// Collectors
			Collector richard = collectors.save(
					Collector.builder()
							.firstName("Richard")
							.lastName("Grayson")
							.email("therealnightwing@gmail.gth")
							.address("730 Franklin Rd. SE, Bludhaven, New Jersey")
							.phone("+1-555-0101").
							build()
			);
			Collector jason = collectors.save(
					Collector.builder()
							.firstName("Jason")
							.lastName("Todd")
							.email("crimealleykid@gmail.gth")
							.address("560 Wayne St. SE, Gotham, New Jersey")
							.phone("+1-555-2020")
							.build()
			);

			// Cards
			cards.save(Card.builder()
					.pokemon("Charizard")
					.grade(9.5)
					.forSale(false)
					.serialNumber("ADF-1121")
					.releaseYear(1999)
					.price(5636)
					.collector(richard)
					.build());
			cards.save(Card.builder()
					.pokemon("Blastoise")
					.grade(8.0)
					.forSale(false)
					.serialNumber("KKO-0212")
					.releaseYear(1999)
					.price(664)
					.collector(jason)
					.build());
			cards.save(Card.builder()
					.pokemon("Umbreon")
					.grade(9.0)
					.forSale(false)
					.serialNumber("ABC-1234")
					.releaseYear(2000)
					.price(1367)
					.collector(jason)
					.build());
			log.info("Seeded {} collectors and {} cards", collectors.count(), cards.count());
		};
	}
}
