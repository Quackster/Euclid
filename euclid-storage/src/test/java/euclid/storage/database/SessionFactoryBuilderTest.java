package euclid.storage.database;

import euclid.storage.database.data.CatalogueData;
import euclid.storage.database.data.ItemData;
import euclid.storage.database.data.ItemDefinitionData;
import euclid.storage.database.data.NavigatorCategoryData;
import euclid.storage.database.data.PlayerData;
import euclid.storage.database.data.RoomData;
import euclid.storage.database.data.RoomModelData;
import euclid.storage.database.data.RoomStatus;
import euclid.storage.database.data.SettingsData;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionFactoryBuilderTest {

    @Test
    void roomStatusEnumValues() {
        assertEquals(0, RoomStatus.OPEN.getValue());
        assertEquals(1, RoomStatus.CLOSED.getValue());
        assertEquals(2, RoomStatus.PASSWORD.getValue());
    }

    @Test
    void roomStatusToEnum() {
        assertEquals(RoomStatus.OPEN, RoomStatus.toStatusEnum(0));
        assertEquals(RoomStatus.CLOSED, RoomStatus.toStatusEnum(1));
        assertEquals(RoomStatus.PASSWORD, RoomStatus.toStatusEnum(2));
        assertEquals(RoomStatus.OPEN, RoomStatus.toStatusEnum(99));
    }

    @Test
    void playerDataHasEntityAnnotation() {
        assertTrue(PlayerData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void roomDataHasEntityAnnotation() {
        assertTrue(RoomData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void itemDataHasEntityAnnotation() {
        assertTrue(ItemData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void itemDefinitionDataHasEntityAnnotation() {
        assertTrue(ItemDefinitionData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void catalogueDataHasEntityAnnotation() {
        assertTrue(CatalogueData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void navigatorCategoryDataHasEntityAnnotation() {
        assertTrue(NavigatorCategoryData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void settingsDataHasEntityAnnotation() {
        assertTrue(SettingsData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void roomModelDataHasEntityAnnotation() {
        assertTrue(RoomModelData.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void playerDataHasIdField() throws NoSuchFieldException {
        Field idField = PlayerData.class.getDeclaredField("id");
        assertNotNull(idField.getAnnotation(Id.class));
    }

    @Test
    void roomDataHasIdField() throws NoSuchFieldException {
        Field idField = RoomData.class.getDeclaredField("id");
        assertNotNull(idField.getAnnotation(Id.class));
    }

    @Test
    void playerDataImplementsIEntityData() {
        assertTrue(euclid.storage.database.data.IEntityData.class.isAssignableFrom(PlayerData.class));
    }
}
