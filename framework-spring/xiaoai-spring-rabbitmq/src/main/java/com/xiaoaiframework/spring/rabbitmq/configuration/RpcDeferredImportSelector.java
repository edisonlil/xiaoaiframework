package com.xiaoaiframework.spring.rabbitmq.configuration;

import com.xiaoaiframework.spring.rabbitmq.annotation.EnableSimpleRpc;
import com.xiaoaiframework.spring.rabbitmq.client.RpcClientScannerRegistrar;
import com.xiaoaiframework.spring.rabbitmq.constant.RpcMode;
import com.xiaoaiframework.spring.rabbitmq.server.RpcServerPostProcessor;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Rpc延时导入选择器
 * 如果需要在所有的@Configuration处理完再导入时可以实现DeferredImportSelector接口。
 * @author edison
 */
@Order
public class RpcDeferredImportSelector implements DeferredImportSelector {


    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        List<String> definitionRegistrars = new ArrayList<>();
        Map<String,Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(EnableSimpleRpc.class.getCanonicalName());

        if(annotationAttributes != null){
            RpcMode[] rpcModes = (RpcMode[]) annotationAttributes.get("mode");
            definitionRegistrars = new ArrayList<>();
            for (RpcMode rpcMode : rpcModes){
                switch (rpcMode){
                    case RPC_CLIENT:
                        definitionRegistrars.add(RpcClientScannerRegistrar.class.getName());
                        break;
                    case RPC_SERVER:
                        definitionRegistrars.add(RpcServerPostProcessor.class.getName());
                        break;
                }
            }

        }
        //将集合转换为字符串数组
        return definitionRegistrars.toArray(new String[0]);
    }
}
