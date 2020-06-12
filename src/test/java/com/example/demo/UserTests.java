package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yangzq80@gmail.com
 * @date 2020-06-12
 */
@Slf4j
public class UserTests {
    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void userIsNull() {
        Person user = new Person();
        user.setId(18);
        Set<ConstraintViolation<Person>> constraintViolationSet = validator.validate(user);
        for (Iterator<ConstraintViolation<Person>> iterator = constraintViolationSet.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<Person> constraintViolation = iterator.next();
            log.info("验证结果,属性:{},结果:{}", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
        }
    }
}
