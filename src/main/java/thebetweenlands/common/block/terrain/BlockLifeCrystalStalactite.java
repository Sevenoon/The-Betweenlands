package thebetweenlands.common.block.terrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.block.property.PropertyBoolUnlisted;
import thebetweenlands.common.block.property.PropertyIntegerUnlisted;
import thebetweenlands.common.item.ItemBlockEnum;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.BlockRegistry.IStateMappedBlock;

public class BlockLifeCrystalStalactite extends BlockSwampWater implements BlockRegistry.ICustomItemBlock, BlockRegistry.ISubtypeBlock, IStateMappedBlock {
	public static final PropertyEnum<EnumLifeCrystalType> VARIANT = PropertyEnum.<EnumLifeCrystalType>create("variant", EnumLifeCrystalType.class);
	public static final PropertyBoolUnlisted NO_BOTTOM = new PropertyBoolUnlisted("no_bottom");
	public static final PropertyBoolUnlisted NO_TOP = new PropertyBoolUnlisted("no_top");
	public static final PropertyIntegerUnlisted DIST_UP = new PropertyIntegerUnlisted("dist_up");
	public static final PropertyIntegerUnlisted DIST_DOWN = new PropertyIntegerUnlisted("dist_down");
	public static final PropertyIntegerUnlisted POS_X = new PropertyIntegerUnlisted("pos_x");
	public static final PropertyIntegerUnlisted POS_Y = new PropertyIntegerUnlisted("pos_x");
	public static final PropertyIntegerUnlisted POS_Z = new PropertyIntegerUnlisted("pos_z");

	public BlockLifeCrystalStalactite(Fluid fluid, Material materialIn) {
		super(fluid, materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumLifeCrystalType.DEFAULT));
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setUnderwaterBlock(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(this, 1, EnumLifeCrystalType.DEFAULT.getMetadata()));
		list.add(new ItemStack(this, 1, EnumLifeCrystalType.ORE.getMetadata()));
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this, 1, ((EnumLifeCrystalType)state.getValue(VARIANT)).getMetadata());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, EnumLifeCrystalType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumLifeCrystalType)state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		ExtendedBlockState state = (ExtendedBlockState) super.createBlockState();
		Collection<IProperty> properties = new ArrayList<IProperty>();
		properties.addAll(state.getProperties());
		properties.add(VARIANT);
		Collection<IUnlistedProperty> unlistedProperties = new ArrayList<IUnlistedProperty>();
		unlistedProperties.addAll(state.getUnlistedProperties());
		unlistedProperties.add(POS_X);
		unlistedProperties.add(POS_Y);
		unlistedProperties.add(POS_Z);
		unlistedProperties.add(NO_BOTTOM);
		unlistedProperties.add(NO_TOP);
		unlistedProperties.add(DIST_UP);
		unlistedProperties.add(DIST_DOWN);
		return new ExtendedBlockState(this, properties.toArray(new IProperty[0]), unlistedProperties.toArray(new IUnlistedProperty[0]));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((EnumLifeCrystalType)state.getValue(VARIANT)).getMetadata();
	}

	public static enum EnumLifeCrystalType implements IStringSerializable {
		DEFAULT(0, "default"),
		ORE(1, "ore");

		private static final EnumLifeCrystalType[] METADATA_LOOKUP = new EnumLifeCrystalType[values().length];
		private final int metadata;
		private final String name;

		private EnumLifeCrystalType(int metadataIn, String nameIn) {
			this.metadata = metadataIn;
			this.name = nameIn;
		}

		public int getMetadata() {
			return this.metadata;
		}

		public String toString() {
			return this.name;
		}

		public static EnumLifeCrystalType byMetadata(int metadata) {
			if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
				metadata = 0;
			}
			return METADATA_LOOKUP[metadata];
		}

		public String getName() {
			return this.name;
		}

		static {
			for (EnumLifeCrystalType type : values()) {
				METADATA_LOOKUP[type.getMetadata()] = type;
			}
		}
	}

	@Override
	public ItemBlock getItemBlock() {
		return ItemBlockEnum.create(this, EnumLifeCrystalType.class);
	}

	@Override
	public boolean isBlockNormalCube(IBlockState blockState) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public IBlockState getExtendedState(IBlockState oldState, IBlockAccess worldIn, BlockPos pos) {
		IExtendedBlockState state = (IExtendedBlockState) super.getExtendedState(oldState, worldIn, pos);

		final int maxLength = 32;
		int distUp = 0;
		int distDown = 0;
		boolean noTop = false;
		boolean noBottom = false;

		IBlockState blockState;
		//Block block;
		for(distUp = 0; distUp < maxLength; distUp++) {
			blockState = worldIn.getBlockState(pos.add(0, 1 + distUp, 0));
			if(blockState.getBlock() == this)
				continue;
			if(blockState.getBlock() == Blocks.AIR || !blockState.isOpaqueCube())
				noTop = true;
			break;
		}
		for(distDown = 0; distDown < maxLength; distDown++)
		{
			blockState = worldIn.getBlockState(pos.add(0, -(1 + distDown), 0));
			if(blockState.getBlock() == this)
				continue;
			if(blockState.getBlock() == Blocks.AIR || !blockState.isOpaqueCube())
				noBottom = true;
			break;
		}

		return state.withProperty(POS_X, pos.getX()).withProperty(POS_Y, pos.getY()).withProperty(POS_Z, pos.getZ()).withProperty(DIST_UP, distUp).withProperty(DIST_DOWN, distDown).withProperty(NO_TOP, noTop).withProperty(NO_BOTTOM, noBottom);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setStateMapper(StateMap.Builder builder) {
		super.setStateMapper(builder);
		builder.ignore(VARIANT);
	}

	@Override
	public int getSubtypeNumber() {
		return EnumLifeCrystalType.values().length;
	}

	@Override
	public String getSubtypeName(int meta) {
		return "%s_" + EnumLifeCrystalType.values()[meta].getName();
	}
}
