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
			if(collectors.count() == 0 && cards.count() == 0){
				// Collectors
				Collector richard = collectors.save(
						Collector.builder()
								.firstName("Richard")
								.lastName("Grayson")
								.email("therealnightwing@gmail.gth")
								.address("730 Franklin Rd. SE, Bludhaven, New Jersey")
								.phone("+1-555-0101")
								.build()
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

				Collector tim = collectors.save(
						Collector.builder()
								.firstName("Tim")
								.lastName("Drake")
								.email("redbirdtech@gmail.gth")
								.address("410 Kane St., Gotham, New Jersey")
								.phone("+1-555-3030")
								.build()
				);

				Collector barbara = collectors.save(
						Collector.builder()
								.firstName("Barbara")
								.lastName("Gordon")
								.email("oracle.ops@gmail.gth")
								.address("120 Clocktower Ave., Gotham, New Jersey")
								.phone("+1-555-4040")
								.build()
				);

				Collector damian = collectors.save(
						Collector.builder()
								.firstName("Damian")
								.lastName("Wayne")
								.email("thedemonspawn@gmail.gth")
								.address("1007 Mountain Drive, Gotham, New Jersey")
								.phone("+1-555-5050")
								.build()
				);

				Collector stephanie = collectors.save(
						Collector.builder()
								.firstName("Stephanie")
								.lastName("Brown")
								.email("spoileralerts@gmail.gth")
								.address("85 Grant St., Gotham, New Jersey")
								.phone("+1-555-6060")
								.build()
				);

				Collector cassandra = collectors.save(
						Collector.builder()
								.firstName("Cassandra")
								.lastName("Cain")
								.email("silentshadow@gmail.gth")
								.address("14 Diamond Hill Rd., Gotham, New Jersey")
								.phone("+1-555-7070")
								.build()
				);

				Collector duke = collectors.save(
						Collector.builder()
								.firstName("Duke")
								.lastName("Thomas")
								.email("signalwatcher@gmail.gth")
								.address("22 Yellowline Blvd., Gotham, New Jersey")
								.phone("+1-555-8080")
								.build()
				);

				Collector kate = collectors.save(
						Collector.builder()
								.firstName("Kate")
								.lastName("Kane")
								.email("batwomanops@gmail.gth")
								.address("900 Foxgate Dr., Gotham, New Jersey")
								.phone("+1-555-9090")
								.build()
				);

				// Cards
				cards.save(Card.builder()
						.pokemon("Charizard")
						.grade(8.0)
						.forSale(true)
						.serialNumber("CHZ-7712")
						.releaseYear(1999)
						.price(3100)
						.collector(richard)
						.build());

				cards.save(Card.builder()
						.pokemon("Charizard")
						.grade(7.5)
						.forSale(false)
						.serialNumber("CHZ-5521")
						.releaseYear(1999)
						.price(2200)
						.collector(jason)
						.build());

				cards.save(Card.builder()
						.pokemon("Charizard")
						.grade(9.0)
						.forSale(false)
						.serialNumber("CHZ-9941")
						.releaseYear(2000)
						.price(4500)
						.collector(damian)
						.build());

				cards.save(Card.builder()
						.pokemon("Blastoise")
						.grade(8.5)
						.forSale(true)
						.serialNumber("BLT-1123")
						.releaseYear(1999)
						.price(780)
						.collector(tim)
						.build());

				cards.save(Card.builder()
						.pokemon("Blastoise")
						.grade(9.0)
						.forSale(false)
						.serialNumber("BLT-1299")
						.releaseYear(1999)
						.price(1100)
						.collector(duke)
						.build());

				cards.save(Card.builder()
						.pokemon("Venusaur")
						.grade(8.0)
						.forSale(true)
						.serialNumber("VNS-8871")
						.releaseYear(1999)
						.price(540)
						.collector(barbara)
						.build());

				cards.save(Card.builder()
						.pokemon("Venusaur")
						.grade(9.0)
						.forSale(false)
						.serialNumber("VNS-6621")
						.releaseYear(1999)
						.price(1200)
						.collector(cassandra)
						.build());

				cards.save(Card.builder()
						.pokemon("Pikachu")
						.grade(9.5)
						.forSale(false)
						.serialNumber("PKA-7723")
						.releaseYear(1999)
						.price(2100)
						.collector(kate)
						.build());

				cards.save(Card.builder()
						.pokemon("Pikachu")
						.grade(8.0)
						.forSale(true)
						.serialNumber("PKA-4420")
						.releaseYear(1999)
						.price(480)
						.collector(richard)
						.build());

				cards.save(Card.builder()
						.pokemon("Pikachu")
						.grade(9.0)
						.forSale(true)
						.serialNumber("PKA-9081")
						.releaseYear(2000)
						.price(620)
						.collector(stephanie)
						.build());

				cards.save(Card.builder()
						.pokemon("Umbreon")
						.grade(8.5)
						.forSale(true)
						.serialNumber("UMB-5501")
						.releaseYear(2000)
						.price(900)
						.collector(jason)
						.build());

				cards.save(Card.builder()
						.pokemon("Umbreon")
						.grade(9.5)
						.forSale(false)
						.serialNumber("UMB-9902")
						.releaseYear(2001)
						.price(3600)
						.collector(tim)
						.build());

				cards.save(Card.builder()
						.pokemon("Espeon")
						.grade(8.0)
						.forSale(true)
						.serialNumber("ESP-4220")
						.releaseYear(2001)
						.price(580)
						.collector(barbara)
						.build());

				cards.save(Card.builder()
						.pokemon("Espeon")
						.grade(9.5)
						.forSale(false)
						.serialNumber("ESP-9001")
						.releaseYear(2001)
						.price(3200)
						.collector(cassandra)
						.build());

				cards.save(Card.builder()
						.pokemon("Lugia")
						.grade(8.5)
						.forSale(true)
						.serialNumber("LUG-6630")
						.releaseYear(2000)
						.price(1500)
						.collector(jason)
						.build());

				cards.save(Card.builder()
						.pokemon("Lugia")
						.grade(9.0)
						.forSale(false)
						.serialNumber("LUG-9901")
						.releaseYear(2000)
						.price(3800)
						.collector(duke)
						.build());

				cards.save(Card.builder()
						.pokemon("Lugia")
						.grade(7.5)
						.forSale(true)
						.serialNumber("LUG-1231")
						.releaseYear(2001)
						.price(980)
						.collector(richard)
						.build());

				cards.save(Card.builder()
						.pokemon("Mewtwo")
						.grade(9.0)
						.forSale(false)
						.serialNumber("MWT-6629")
						.releaseYear(1999)
						.price(1200)
						.collector(kate)
						.build());

				cards.save(Card.builder()
						.pokemon("Mewtwo")
						.grade(8.0)
						.forSale(true)
						.serialNumber("MWT-5520")
						.releaseYear(1999)
						.price(580)
						.collector(stephanie)
						.build());

				cards.save(Card.builder()
						.pokemon("Mewtwo")
						.grade(9.5)
						.forSale(false)
						.serialNumber("MWT-9912")
						.releaseYear(2000)
						.price(2500)
						.collector(damian)
						.build());

				cards.save(Card.builder()
						.pokemon("Mew")
						.grade(9.0)
						.forSale(true)
						.serialNumber("MEW-1131")
						.releaseYear(2000)
						.price(900)
						.collector(cassandra)
						.build());

				cards.save(Card.builder()
						.pokemon("Mew")
						.grade(8.5)
						.forSale(false)
						.serialNumber("MEW-8844")
						.releaseYear(2000)
						.price(720)
						.collector(tim)
						.build());

				cards.save(Card.builder()
						.pokemon("Rayquaza")
						.grade(9.5)
						.forSale(false)
						.serialNumber("RYQ-8811")
						.releaseYear(2003)
						.price(4200)
						.collector(jason)
						.build());

				cards.save(Card.builder()
						.pokemon("Rayquaza")
						.grade(8.0)
						.forSale(true)
						.serialNumber("RYQ-3321")
						.releaseYear(2003)
						.price(970)
						.collector(barbara)
						.build());

				cards.save(Card.builder()
						.pokemon("Rayquaza")
						.grade(9.0)
						.forSale(false)
						.serialNumber("RYQ-7744")
						.releaseYear(2003)
						.price(2100)
						.collector(richard)
						.build());

				cards.save(Card.builder()
						.pokemon("Gengar")
						.grade(9.0)
						.forSale(true)
						.serialNumber("GNG-9921")
						.releaseYear(2002)
						.price(780)
						.collector(duke)
						.build());

				cards.save(Card.builder()
						.pokemon("Gengar")
						.grade(8.0)
						.forSale(true)
						.serialNumber("GNG-5510")
						.releaseYear(2002)
						.price(430)
						.collector(kate)
						.build());

				cards.save(Card.builder()
						.pokemon("Gengar")
						.grade(9.5)
						.forSale(false)
						.serialNumber("GNG-1900")
						.releaseYear(2000)
						.price(1800)
						.collector(damian)
						.build());

				cards.save(Card.builder()
						.pokemon("Dragonite")
						.grade(8.0)
						.forSale(true)
						.serialNumber("DRG-7121")
						.releaseYear(1999)
						.price(580)
						.collector(tim)
						.build());

				cards.save(Card.builder()
						.pokemon("Dragonite")
						.grade(9.0)
						.forSale(false)
						.serialNumber("DRG-9920")
						.releaseYear(1999)
						.price(1400)
						.collector(cassandra)
						.build());

				cards.save(Card.builder()
						.pokemon("Gyarados")
						.grade(8.5)
						.forSale(true)
						.serialNumber("GYR-8810")
						.releaseYear(1999)
						.price(620)
						.collector(richard)
						.build());

				cards.save(Card.builder()
						.pokemon("Gyarados")
						.grade(9.0)
						.forSale(false)
						.serialNumber("GYR-5500")
						.releaseYear(1999)
						.price(900)
						.collector(jason)
						.build());

				cards.save(Card.builder()
						.pokemon("Snorlax")
						.grade(8.0)
						.forSale(true)
						.serialNumber("SNL-2211")
						.releaseYear(1999)
						.price(300)
						.collector(barbara)
						.build());

				cards.save(Card.builder()
						.pokemon("Snorlax")
						.grade(9.0)
						.forSale(false)
						.serialNumber("SNL-9910")
						.releaseYear(2000)
						.price(780)
						.collector(duke)
						.build());

				cards.save(Card.builder()
						.pokemon("Arcanine")
						.grade(8.5)
						.forSale(true)
						.serialNumber("ARC-1181")
						.releaseYear(1999)
						.price(420)
						.collector(kate)
						.build());

				cards.save(Card.builder()
						.pokemon("Arcanine")
						.grade(9.0)
						.forSale(false)
						.serialNumber("ARC-5519")
						.releaseYear(1999)
						.price(820)
						.collector(stephanie)
						.build());

				cards.save(Card.builder()
						.pokemon("Tyranitar")
						.grade(7.5)
						.forSale(true)
						.serialNumber("TYR-8891")
						.releaseYear(2002)
						.price(260)
						.collector(damian)
						.build());

				cards.save(Card.builder()
						.pokemon("Tyranitar")
						.grade(9.0)
						.forSale(false)
						.serialNumber("TYR-9922")
						.releaseYear(2000)
						.price(1250)
						.collector(cassandra)
						.build());

				cards.save(Card.builder()
						.pokemon("Ho-Oh")
						.grade(9.0)
						.forSale(false)
						.serialNumber("HOO-1288")
						.releaseYear(2000)
						.price(2100)
						.collector(richard)
						.build());

				cards.save(Card.builder()
						.pokemon("Ho-Oh")
						.grade(8.0)
						.forSale(true)
						.serialNumber("HOO-5599")
						.releaseYear(2001)
						.price(800)
						.collector(jason)
						.build());

				cards.save(Card.builder()
						.pokemon("Celebi")
						.grade(9.0)
						.forSale(true)
						.serialNumber("CEL-7722")
						.releaseYear(2001)
						.price(460)
						.collector(tim)
						.build());

				cards.save(Card.builder()
						.pokemon("Celebi")
						.grade(8.5)
						.forSale(false)
						.serialNumber("CEL-5518")
						.releaseYear(2001)
						.price(410)
						.collector(barbara)
						.build());

				cards.save(Card.builder()
						.pokemon("Latios")
						.grade(9.5)
						.forSale(false)
						.serialNumber("LTS-9928")
						.releaseYear(2003)
						.price(1500)
						.collector(duke)
						.build());

				cards.save(Card.builder()
						.pokemon("Latias")
						.grade(9.0)
						.forSale(true)
						.serialNumber("LAT-4471")
						.releaseYear(2003)
						.price(720)
						.collector(kate)
						.build());



				log.info("Seeded {} collectors and {} cards", collectors.count(), cards.count());
			}
		};
	}
}
