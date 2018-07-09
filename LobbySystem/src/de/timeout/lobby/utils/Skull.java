package de.timeout.lobby.utils;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

public enum Skull {

    ARROW_LEFT("MHF_ArrowLeft"),
    ARROW_RIGHT("MHF_ArrowRight"),
    ARROW_UP("MHF_ArrowUp"),
    ARROW_DOWN("MHF_ArrowDown"),
    QUESTION("MHF_Question"),
    EXCLAMATION("MHF_Exclamation"),
    CAMERA("FHG_Cam"),

    ZOMBIE_PIGMAN("MHF_PigZombie"),
    PIG("MHF_Pig"),
    SHEEP("MHF_Sheep"),
    BLAZE("MHF_Blaze"),
    CHICKEN("MHF_Chicken"),
    COW("MHF_Cow"),
    SLIME("MHF_Slime"),
    SPIDER("MHF_Spider"),
    SQUID("MHF_Squid"),
    VILLAGER("MHF_Villager"),
    OCELOT("MHF_Ocelot"),
    HEROBRINE("MHF_Herobrine"),
    LAVA_SLIME("MHF_LavaSlime"),
    MOOSHROOM("MHF_MushroomCow"),
    GOLEM("MHF_Golem"),
    GHAST("MHF_Ghast"),
    ENDERMAN("MHF_Enderman"),
    CAVE_SPIDER("MHF_CaveSpider"),

    CACTUS("MHF_Cactus"),
    CAKE("MHF_Cake"),
    CHEST("MHF_Chest"),
    MELON("MHF_Melon"),
    LOG("MHF_OakLog"),
    PUMPKIN("MHF_Pumpkin"),
    TNT("MHF_TNT"),
    DYNAMITE("MHF_TNT2");
	
    private static final Base64 base64 = new Base64();
    private static final String TEXTURES = "textures";
    
    private String id;

    private Skull(String id) {
        this.id = id;
    }

    public static ItemStack getCustomSkull(String url) throws IllegalAccessException {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put(TEXTURES, new Property(TEXTURES, new String(encodedData)));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        head.setItemMeta(headMeta);
        return head;
    }
    
	public static ItemStack getSkull(GameProfile profile, String display) {
		try {
			Property property = profile.getProperties().get(TEXTURES).iterator().next();
			String value = property.getValue();
			String base64decoded = new String(Base64.decodeBase64(value));
			JsonObject json = new JsonParser().parse(base64decoded).getAsJsonObject();
			String url = json.get(TEXTURES).getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
			ItemStack skull = Skull.getCustomSkull(url);
			
			ItemMeta skullMeta = skull.getItemMeta();
			skullMeta.setDisplayName(display);
			skull.setItemMeta(skullMeta);
			return skull;
		} catch (IllegalAccessException e) {}	
		return ItemStackAPI.createItemStack(Material.SKULL_ITEM, (short) 3, 1, "");
	}
    
    public String getId() {
        return id;
    }
}