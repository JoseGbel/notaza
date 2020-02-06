package remcode.apps.notaza.presentation.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {

            // checking if what we are touching is a category or a skill
            if (viewHolder.getClass() == CategoryListAdapter.CategoryViewHolder.class) {
                final View foregroundView = ((CategoryListAdapter.CategoryViewHolder) viewHolder).itemView;
                getDefaultUIUtil().onSelected(foregroundView);
            } else {
                final View foregroundView = ((SkillListAdapter.SkillViewHolder) viewHolder).itemView;
                getDefaultUIUtil().onSelected(foregroundView);
            }

        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

        if (viewHolder.getClass() == SkillListAdapter.SkillViewHolder.class) {
            final View foregroundView = ((SkillListAdapter.SkillViewHolder) viewHolder).skillLinearLayout;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }else {
            final View foregroundView = ((CategoryListAdapter.CategoryViewHolder) viewHolder).categoryCardView;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);

        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (viewHolder.getClass() == SkillListAdapter.SkillViewHolder.class) {
            final View foregroundView = ((SkillListAdapter.SkillViewHolder) viewHolder).skillLinearLayout;
            getDefaultUIUtil().clearView(foregroundView);
        }else{
            final View foregroundView = ((CategoryListAdapter.CategoryViewHolder) viewHolder).categoryCardView;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        //Using the same class for determine whether is a skill or a category what we are touching
        if (viewHolder.getClass() == SkillListAdapter.SkillViewHolder.class) {
            final View foregroundView = ((SkillListAdapter.SkillViewHolder) viewHolder).skillLinearLayout;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }else{
            final View foregroundView = ((CategoryListAdapter.CategoryViewHolder) viewHolder).categoryCardView;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}