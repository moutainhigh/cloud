package com.smart4y.cloud.core.toolkit.secret;

import lombok.Value;

/**
 * @author Youtao
 *         Created by youtao on 2018/12/13.
 */
@Value
public class RsaData {

    private String pubKey;
    private String priKey;
}