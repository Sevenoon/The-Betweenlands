package thebetweenlands.common.block.terrain;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.item.ItemBlockEnum;
import thebetweenlands.common.registries.BlockRegistry;

public class BlockGenericStone extends Block implements BlockRegistry.ICustomItemBlock, BlockRegistry.ISubtypeBlock{

	public static final PropertyBool IS_SPOOPY = PropertyBool.create("isspoopy");
	public static final PropertyEnum<EnumStoneType> VARIANT = PropertyEnum.<EnumStoneType>create("variant", EnumStoneType.class);

	public BlockGenericStone() {
		super(Material.ROCK);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 0);
		setCreativeTab(BLCreativeTabs.BLOCKS);
		//setBlockName("thebetweenlands.genericStone");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumStoneType.CORRUPT_BETWEENSTONE).withProperty(IS_SPOOPY, false));
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(Item id, CreativeTabs tab, List list) {
		for (EnumStoneType type : EnumStoneType.values())
			list.add(new ItemStack(id, 1, type.getMetadata()));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {VARIANT, IS_SPOOPY});
	}

	protected ItemStack createStackedBlock(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this), 1, ((EnumStoneType)state.getValue(VARIANT)).getMetadata());
	}

	public int damageDropped(IBlockState state) {
		return ((EnumStoneType)state.getValue(VARIANT)).getMetadata();
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMetadata();
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(IS_SPOOPY, false).withProperty(VARIANT, EnumStoneType.byMetadata(meta));
	}

	@Override
	public ItemBlock getItemBlock() {
		return ItemBlockEnum.create(this, EnumStoneType.class);
	}

	/*public static enum EnumStoneType implements IStringSerializable{
		CORRUPT_BETWEENSTONE;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}

	}*/

	public static enum EnumStoneType implements IStringSerializable {
		CORRUPT_BETWEENSTONE(0);

		private static final EnumStoneType[] METADATA_LOOKUP = new EnumStoneType[values().length];
		private final int metadata;
		private final String name;

		private EnumStoneType(int metadataIn) {
			this.metadata = metadataIn;
			this.name = this.name().toLowerCase(Locale.ENGLISH);
		}

		public int getMetadata() {
			return this.metadata;
		}

		public static EnumStoneType byMetadata(int metadata) {
			if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
				metadata = 0;
			}
			return METADATA_LOOKUP[metadata];
		}

		@Override
		public String getName() {
			return this.name;
		}

		static {
			for (EnumStoneType type : values()) {
				METADATA_LOOKUP[type.getMetadata()] = type;
			}
		}
	}

	@Override
	public int getSubtypeNumber() {
		return EnumStoneType.values().length;
	}

	@Override
	public String getSubtypeName(int meta) {
		return EnumStoneType.byMetadata(meta).getName();
	}
}
