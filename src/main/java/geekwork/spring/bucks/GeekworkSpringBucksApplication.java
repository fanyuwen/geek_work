package geekwork.spring.bucks;

import geekwork.spring.bucks.enums.OrderState;
import geekwork.spring.bucks.mapper.CoffeeMapper;
import geekwork.spring.bucks.model.Coffee;
import geekwork.spring.bucks.model.CoffeeExample;
import geekwork.spring.bucks.model.Order;
import geekwork.spring.bucks.service.CoffeeOrderService;
import geekwork.spring.bucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("geekwork.spring.bucks.mapper")
public class GeekworkSpringBucksApplication implements ApplicationRunner {

    @Autowired
    private CoffeeMapper coffeeMapper;

    @Autowired
    private CoffeeService coffeeService;

    @Autowired
    private CoffeeOrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(GeekworkSpringBucksApplication.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        log.info("All Coffee: {}", coffeeMapper.selectByExample(new CoffeeExample()));

        Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
        if (latte.isPresent()) {
            Order order = orderService.createOrder("Li Lei", latte.get());
            log.info("Update INIT to PAID: {}", orderService.updateState(order, OrderState.PAID));
            log.info("Update PAID to INIT: {}", orderService.updateState(order, OrderState.INIT));
        }
    }

    private void generateArtifacts() throws Exception {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(
                this.getClass().getResourceAsStream("/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
