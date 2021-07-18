package com.example.academey.utils

import com.example.academey.config.FlywayMigrationConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringExtensionTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(FlywayMigrationConfiguration::class)
annotation class SpringExtensionTestWithJpa
