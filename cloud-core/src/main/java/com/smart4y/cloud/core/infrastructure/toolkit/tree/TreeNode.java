package com.smart4y.cloud.core.infrastructure.toolkit.tree;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Data
@Accessors(chain = true)
public class TreeNode implements Serializable {

    /**
     * 节点ID
     */
    protected Long id;
    /**
     * 父级节点ID
     */
    protected Long parentId;
    /**
     * 子节点
     */
    protected List<TreeNode> children = new ArrayList<>();

    /**
     * 添加节点
     */
    public void add(TreeNode node) {
        children.add(node);
    }
}