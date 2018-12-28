/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bid.adonis.lau.mongodb.order;

import bid.adonis.lau.mongodb.AbstractIntegrationTest;
import bid.adonis.lau.mongodb.core.*;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static bid.adonis.lau.mongodb.core.CoreMatchers.named;
import static bid.adonis.lau.mongodb.core.CoreMatchers.with;
import static bid.adonis.lau.mongodb.order.OrderMatchers.containsOrder;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link OrderRepository}.
 * 
 * @author Oliver Gierke
 */
public class OrderRepositoryIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	OrderRepository repository;

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ProductRepository productRepository;

	@Test
	public void createOrder() {

		Customer dave = customerRepository.findByEmailAddress(new EmailAddress("dave@dmband.com"));
		Product iPad = productRepository.findAll().iterator().next();

		Order order = new Order(dave, dave.getAddresses().iterator().next());
		order.add(new LineItem(iPad));

		order = repository.save(order);
		assertThat(order.getId(), is(notNullValue()));
	}

	@Test
	public void readOrder() {

		Customer dave = customerRepository.findByEmailAddress(new EmailAddress("dave@dmband.com"));
		List<Order> orders = repository.findByCustomer(dave);
		Matcher<Iterable<? super Order>> hasOrderForiPad = containsOrder(with(LineItem(with(Product(named("iPad"))))));

		assertThat(orders, hasSize(1));
		assertThat(orders, hasOrderForiPad);
	}
}
