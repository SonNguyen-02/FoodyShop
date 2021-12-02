package com.example.foodyshop.service;

import static com.example.foodyshop.config.Config.BASE_API;

public class APIService {

    public static com.example.foodyshop.service.DataService getService(){
        return APIRetrofitClient.getClient(BASE_API).create(DataService.class);
    }
}
