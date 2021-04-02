package org.acme;

import org.eclipse.microprofile.faulttolerance.Retry;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/cars")
public class CarResource {
    @Inject
    CarRepository carRepository;

    @GET
    @Retry
    @Transactional
    public String getCarByName(@QueryParam("name") String name) {
        System.out.println(">>> Searching car by name: " + name);

        var car = carRepository.findByName(name).orElseGet(() -> createAndSaveCar(name));

        return car.name;
    }

    private Car createAndSaveCar(String name) {
        System.out.println(">>> No car found named: " + name);
        System.out.println(">>> Inserting new car named: " + name);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var car = new Car();

        car.name = name;

        carRepository.persist(car);

        return car;
    }
}