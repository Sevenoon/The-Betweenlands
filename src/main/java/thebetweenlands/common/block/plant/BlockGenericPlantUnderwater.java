package thebetweenlands.common.block.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.block.BlockStateContainerHelper;
import thebetweenlands.common.block.SoilHelper;
import thebetweenlands.common.block.terrain.BlockSwampWater;
import thebetweenlands.common.registries.BlockRegistry.IStateMappedBlock;

public class BlockGenericPlantUnderwater extends BlockSwampWater implements net.minecraftforge.common.IPlantable, IStateMappedBlock {
	public BlockGenericPlantUnderwater(Fluid fluid, Material materialIn) {
		super(fluid, materialIn);
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setUnderwaterBlock(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
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
	public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return net.minecraftforge.common.EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) return getDefaultState();
		return state;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.NONE;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setStateMapper(StateMap.Builder builder) {
		super.setStateMapper(builder);
		builder.ignore(LEVEL);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged(state, worldIn, pos, blockIn);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		{
			IBlockState soil = worldIn.getBlockState(pos.down());
			return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
		}
		return this.canSustainPlant(worldIn.getBlockState(pos.down()));
	}

	protected boolean canSustainPlant(IBlockState state) {
		return SoilHelper.canSustainUnderwaterPlant(state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos).getMaterial() == Material.WATER && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
		return super.canSustainPlant(state, world, pos, direction, plantable) || (plantable instanceof BlockGenericPlantUnderwater && ((BlockGenericPlantUnderwater)plantable).canSustainPlant(state));
	}
}
