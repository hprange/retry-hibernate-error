package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {
    public Optional<Car> findByName(String name) {
        try (var cars = stream("name = ?1", name)) {
            return cars.findAny();
        }
    }
}
