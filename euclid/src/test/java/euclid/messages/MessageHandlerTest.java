package euclid.messages;

import euclid.messages.headers.IncomingEvents;
import euclid.messages.headers.OutgoingEvents;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MessageHandlerTest {

    @Test
    void everyIncomingHandlerClassNameResolvesToHeaderConstant() throws Exception {
        Set<String> incomingConstants = getConstantValues(IncomingEvents.class);

        List<Class<? extends IMessageEvent>> handlers = List.of(
                euclid.messages.incoming.VERSIONCHECK.class,
                euclid.messages.incoming.FINDUSER.class,
                euclid.messages.incoming.APPROVENAME.class,
                euclid.messages.incoming.REGISTER.class,
                euclid.messages.incoming.UPDATE.class,
                euclid.messages.incoming.LOGIN.class,
                euclid.messages.incoming.INFORETRIEVE.class,
                euclid.messages.incoming.INITUNITLISTENER.class,
                euclid.messages.incoming.Move.class,
                euclid.messages.incoming.GOAWAY.class,
                euclid.messages.incoming.CLIENTIP.class,
                euclid.messages.incoming.CREATEFLAT.class,
                euclid.messages.incoming.TRYFLAT.class,
                euclid.messages.incoming.GOTOFLAT.class,
                euclid.messages.incoming.SEARCHBUSYFLATS.class,
                euclid.messages.incoming.SETFLATINFO.class,
                euclid.messages.incoming.GETFLATINFO.class,
                euclid.messages.incoming.UPDATEFLAT.class,
                euclid.messages.incoming.GETORDERINFO.class,
                euclid.messages.incoming.GETCREDITS.class,
                euclid.messages.incoming.CHAT.class,
                euclid.messages.incoming.SHOUT.class,
                euclid.messages.incoming.WHISPER.class,
                euclid.messages.incoming.DELETEFLAT.class,
                euclid.messages.incoming.GETSTRIP.class,
                euclid.messages.incoming.PLACESTUFFFROMSTRIP.class,
                euclid.messages.incoming.PLACEITEMFROMSTRIP.class,
                euclid.messages.incoming.MOVESTUFF.class,
                euclid.messages.incoming.ADDSTRIPITEM.class,
                euclid.messages.incoming.PURCHASE.class,
                euclid.messages.incoming.FLATPROPERTYBYITEM.class
        );

        for (Class<? extends IMessageEvent> handler : handlers) {
            String className = handler.getSimpleName();
            assertTrue(incomingConstants.contains(className),
                    "Incoming handler " + className + " has no matching IncomingEvents constant");
        }

        assertEquals(incomingConstants.size(), handlers.size(),
                "Mismatch between IncomingEvents constants and handler classes");
    }

    @Test
    void everyOutgoingComposerClassNameResolvesToHeaderConstant() throws Exception {
        Set<String> outgoingConstants = getConstantValues(OutgoingEvents.class);

        List<Class<? extends IMessageComposer>> composers = List.of(
                euclid.messages.outgoing.HELLO.class,
                euclid.messages.outgoing.SECRET_KEY.class,
                euclid.messages.outgoing.ENCRYPTION_ON.class,
                euclid.messages.outgoing.ENCRYPTION_OFF.class,
                euclid.messages.outgoing.NAME_APPROVED.class,
                euclid.messages.outgoing.NAME_UNACCEPTABLE.class,
                euclid.messages.outgoing.SYSTEMBROADCAST.class,
                euclid.messages.outgoing.USEROBJECT.class,
                euclid.messages.outgoing.ALLUNITS.class,
                euclid.messages.outgoing.OBJECTS.class,
                euclid.messages.outgoing.USERS.class,
                euclid.messages.outgoing.STATUS.class,
                euclid.messages.outgoing.HEIGHTMAP.class,
                euclid.messages.outgoing.LOGOUT.class,
                euclid.messages.outgoing.FLATCREATED.class,
                euclid.messages.outgoing.FLAT_LETIN.class,
                euclid.messages.outgoing.BUSY_FLAT_RESULTS.class,
                euclid.messages.outgoing.FLATINFO.class,
                euclid.messages.outgoing.ORDERINFO.class,
                euclid.messages.outgoing.WALLETBALANCE.class,
                euclid.messages.outgoing.CHAT.class,
                euclid.messages.outgoing.SHOUT.class,
                euclid.messages.outgoing.WHISPER.class,
                euclid.messages.outgoing.ACTIVE_OBJECTS.class,
                euclid.messages.outgoing.ITEMS.class,
                euclid.messages.outgoing.YOUAREOWNER.class,
                euclid.messages.outgoing.YOUARECONTROLLER.class,
                euclid.messages.outgoing.STRIPINFO.class,
                euclid.messages.outgoing.ACTIVEOBJECT_ADD.class,
                euclid.messages.outgoing.ACTIVEOBJECT_UPDATE.class,
                euclid.messages.outgoing.ADDITEM.class,
                euclid.messages.outgoing.ACTIVEOBJECT_REMOVE.class,
                euclid.messages.outgoing.REMOVEITEM.class,
                euclid.messages.outgoing.ADDSTRIPITEM.class,
                euclid.messages.outgoing.FLATPROPERTY.class
        );

        for (Class<? extends IMessageComposer> composer : composers) {
            String className = composer.getSimpleName();
            assertTrue(outgoingConstants.contains(className),
                    "Outgoing composer " + className + " has no matching OutgoingEvents constant");
        }

        assertEquals(outgoingConstants.size(), composers.size(),
                "Mismatch between OutgoingEvents constants and composer classes");
    }

    @Test
    void messageHandlerRegistersAllEventsAndComposers() throws Exception {
        MessageHandler handler = new MessageHandler();
        handler.resolveMessages();

        Set<String> incomingConstants = getConstantValues(IncomingEvents.class);
        Set<String> outgoingConstants = getConstantValues(OutgoingEvents.class);

        for (String constant : incomingConstants) {
            assertNotNull(handler.getEvent(constant),
                    "No event registered for header: " + constant);
        }

        for (String constant : outgoingConstants) {
            assertNotNull(handler.getComposerByHeader(constant),
                    "No composer registered for header: " + constant);
        }
    }

    private static Set<String> getConstantValues(Class<?> clazz) throws Exception {
        Set<String> values = new HashSet<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    Modifier.isFinal(field.getModifiers()) &&
                    field.getType() == String.class) {
                values.add((String) field.get(null));
            }
        }
        return values;
    }
}
