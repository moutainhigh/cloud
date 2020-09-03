package com.smart4y.cloud.core.toolkit.idcard;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Youtao
 *         Created by youtao on 2018/8/28.
 */
@Slf4j
public enum IdCardHelperImpl implements IdCardHelper {

    INSTANCE;

    @Override
    public IdentityCard of(String idNumber) {
        IdentityNumberParser parser = IdentityNumberParser.of(idNumber);
        return new IdentityCard(parser.idNumber(), parser.province(), parser.city(), parser.region(),
                parser.year(), parser.month(), parser.day(), parser.gender(), parser.birthday(), parser.age());
    }
}