package com.xiaoaiframework.spring.mongo.context;

import org.springframework.data.mongodb.core.query.Update;

public class UpdateContext extends QuerySelectContext{

      Update update = null;


      public void setUpdate(Update update) {
            this.update = update;
      }

      public Update getUpdate() {
            return update;
      }

}
