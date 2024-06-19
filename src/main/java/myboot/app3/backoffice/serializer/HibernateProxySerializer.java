package myboot.app3.backoffice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HibernateProxySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object entity, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (entity instanceof HibernateProxy) {
            // If the entity is a Hibernate proxy, unwrap it to get the actual entity object
            entity = Hibernate.unproxy(entity);
        }
        // Serialize the entity ID as a number
        Long id = null;
        try {
            id = (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            gen.writeNull();
            return;
        }
        gen.writeNumber(id);
    }
}