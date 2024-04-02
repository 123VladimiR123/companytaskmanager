package com.spring.companytaskmanager.queryconverts;

import com.spring.companytaskmanager.staticnotservices.QueryConverter;
import org.junit.jupiter.api.Test;

class QueryConverterTest {
    @Test
    void convert1() {
        System.out.println(QueryConverter.convert("Кубасов Владимир Юрьевич"));
    }

    @Test
    void convert2() {
        System.out.println(QueryConverter.convert(" "));
    }

    @Test
    void convert3() {
        System.out.println(QueryConverter.convert(""));
    }

    @Test
    void convert4() {
        System.out.println(QueryConverter.convert("КуБаСОВ   ..."));
    }

    @Test
    void convert5() {
        System.out.println(QueryConverter.convert("ишмала.ишмала ишмала куды куба"));
    }

    @Test
    void convert6() {
        System.out.println(QueryConverter.convert("очень много слов в этой строке"));
    }

    @Test
    void convert7() {
        System.out.println(QueryConverter.convert("   ... . '''"));
    }
}