package thebetweenlands.client.tabs;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import thebetweenlands.client.tab.CreativeTabBetweenlands;

public class TabSpecialItems extends CreativeTabBetweenlands {
	public TabSpecialItems() {
		super("thebetweenlands.special");
	}

	@Override
	public Item getTabIconItem() {
		return /*BLItemRegistry.shimmerStone*/Item.getItemFromBlock(Blocks.STONE);
	}
}
