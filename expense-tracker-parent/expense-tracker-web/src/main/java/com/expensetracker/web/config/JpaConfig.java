package com.expensetracker.web.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;               // CORRECT!
import jakarta.persistence.EntityManagerFactory; 


import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public DataSource dataSource() {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        // Replace with your actual Wildfly JNDI datasource name!
        return dataSourceLookup.getDataSource("java:jboss/datasources/ExpenseDS");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.expensetracker.auth.entity",     "com.expensetracker.model"
);
        em.setPersistenceUnitName("default"); // This must match your persistence.xml
        // Optionally set vendor adapter, properties, etc.
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
