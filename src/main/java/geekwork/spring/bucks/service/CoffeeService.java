package geekwork.spring.bucks.service;

import geekwork.spring.bucks.mapper.CoffeeMapper;
import geekwork.spring.bucks.model.Coffee;
import geekwork.spring.bucks.model.CoffeeExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeMapper coffeeMapper;

    @Cacheable
    public Optional<Coffee> findOneCoffee(String name) {
        CoffeeExample coffeeExample = new CoffeeExample();
        coffeeExample.createCriteria().andNameEqualTo("latte");
        List<Coffee> list = coffeeMapper.selectByExample(coffeeExample);
        Optional<Coffee> coffee = list.stream().findFirst();
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }
}
