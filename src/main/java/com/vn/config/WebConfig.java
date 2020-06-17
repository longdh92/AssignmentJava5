package com.vn.config;

import com.vn.repository.*;
import com.vn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories("com.vn.repository")
@ComponentScan("com.vn")
@EnableSpringDataWebSupport
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public AdminRepository adminRepository() {
        return new AdminRepositoryImpl();
    }

    @Bean
    public AdminService adminService() {
        return new AdminServiceImpl();
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new CustomerRepositoryImpl();
    }

    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImpl();
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return new CategoryRepositoryImpl();
    }

    @Bean
    public CategoryService categoryService() {
        return new CategoryServiceImpl();
    }

    @Bean
    public ProductRepository productRepository() {
        return new ProductRepositoryImpl();
    }

    @Bean
    public ProductService productService() {
        return new ProductServiceImpl();
    }

    @Bean
    public CartDetailRepository cartDetailRepository() {
        return new CartDetailRepositoryImpl();
    }

    @Bean
    public CartDetailService cartDetailService() {
        return new CartDetailServiceImpl();
    }

    @Bean
    public CartRepository cartRepository() {
        return new CartRepositoryImpl();
    }

    @Bean
    public CartService cartService() {
        return new CartServiceImpl();
    }

    @Bean
    public InvoiceRepository invoiceRepository() {
        return new InvoiceRepositoryImpl();
    }

    @Bean
    public InvoiceService invoiceService() {
        return new InvoiceServiceImpl();
    }

    @Bean
    public InvoiceDetailRepository invoiceDetailRepository() {
        return new InvoiceDetailRepositoryImpl();
    }

    @Bean
    public InvoiceDetailService invoiceDetailService() {
        return new InvoiceDetailServiceImpl();
    }

    @Bean
    public InvoiceStatusRepository invoiceStatusRepository() {
        return new InvoiceStatusRepositoryImpl();
    }

    @Bean
    public InvoiceStatusService invoiceStatusService() {
        return new InvoiceStatusServiceImpl();
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setContentType("text/html; charset=UTF-8");
        viewResolver.setTemplateEngine((ISpringTemplateEngine) templateEngine());
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/resourcesAdmin/**")
                .addResourceLocations("/WEB-INF/views/resources/", "/WEB-INF/views/admin/resourcesAdmin/");
    }

    //Cấu hình JPA
    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/AssignmentJava5?useUnicode=yes&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emBean = new LocalContainerEntityManagerFactoryBean();
        emBean.setDataSource(dataSource());
        emBean.setPackagesToScan(new String[]{"com.vn.model"});

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emBean.setJpaVendorAdapter(vendorAdapter);
        emBean.setJpaProperties(additionalProperties());
        return emBean;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(10000000);
        return multipartResolver;
    }
}
