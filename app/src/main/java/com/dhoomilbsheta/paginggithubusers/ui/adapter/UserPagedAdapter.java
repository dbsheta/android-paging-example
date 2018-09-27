package com.dhoomilbsheta.paginggithubusers.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dhoomilbsheta.paginggithubusers.R;
import com.dhoomilbsheta.paginggithubusers.data.NetworkState;
import com.dhoomilbsheta.paginggithubusers.vo.User;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class UserPagedAdapter extends PagedListAdapter<User, RecyclerView.ViewHolder> {
    private static final String TAG = "UserAdapter";
    private NetworkState networkState;

    public UserPagedAdapter() {
        super(User.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == R.layout.item_user) {
            view = inflater.inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == R.layout.item_network_state) {
            view = inflater.inflate(R.layout.item_network_state, parent, false);
            return new NetworkStateViewHolder(view);
        } else {
            throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_user:
                ((UserViewHolder) holder).bind(getItem(position));
                break;
            case R.layout.item_network_state:
                ((NetworkStateViewHolder) holder).bind(networkState);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_network_state;
        }
        return R.layout.item_user;
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userId;

        UserViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            userId = view.findViewById(R.id.userId);
        }

        void bind(User user) {
            userName.setText(user.getFirstName());
            userId.setText(String.valueOf(user.getId()));
        }
    }

    private class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;
        private final TextView errorMsg;
        private Button button;

        NetworkStateViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progress_bar);
            errorMsg = view.findViewById(R.id.error_msg);
            button = view.findViewById(R.id.retry_button);
        }

        void bind(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(networkState.getMsg());
            } else {
                errorMsg.setVisibility(View.GONE);
            }
        }
    }

    public void setNetworkState(NetworkState networkState) {
        NetworkState prevState = this.networkState;
        boolean prevExtraRow = hasExtraRow();
        this.networkState = networkState;
        boolean newExtraRow = hasExtraRow();

        if (prevExtraRow != newExtraRow) {
            if (prevExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && prevState != networkState) {
            notifyItemChanged(getItemCount() - 1);
        }

    }
}
