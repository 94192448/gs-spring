package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author yangzq80@gmail.com
 * @date 2020-06-12
 */
@Slf4j
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, Person> {

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {

        if (person.id>10){
            if (!StringUtils.isEmpty(person.name)){
                return true;
            }
        }
        //非法信息
        String messageTemplate=String.format("用户名：%1$s和ID：%2$s不匹配",person.getName(),person.getId());
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();

        return false;
    }
}
